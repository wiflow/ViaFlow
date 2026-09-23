package dev.wiflow.viaflow.listener;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.translation.ConnectionHooks;
import dev.wiflow.viaflow.version.AutoChoice;
import dev.wiflow.viaflow.version.NativeVersion;
import dev.wiflow.viaflow.version.SelectableVersions;
import dev.wiflow.viaflow.version.ServerDetails;
import dev.wiflow.viaflow.version.ServerMemory;
import dev.wiflow.viaflow.version.ServerVersionDetector;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import net.labymod.api.Constants;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.server.ConnectableServerData;
import net.labymod.api.client.network.server.ServerAddress;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkPayloadEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.ServerKickEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;

public class ConnectionListener {

    // Velocity turns away a second login from the same address within three seconds by default.
    private static final long LOGIN_LIMIT_MILLIS = 3500;

    private final ViaFlowAddon addon;
    private final ServerMemory memory;
    private final AtomicReference<ConnectableServerData> retryServer = new AtomicReference<>();

    // What Auto joins each server as for the rest of the session.
    private final Map<ServerAddress, AutoChoice> choices = new ConcurrentHashMap<>();

    private volatile Attempt attempt;
    private volatile ServerAddress currentServer;

    public ConnectionListener(ViaFlowAddon addon) {
        this.addon = addon;
        this.memory = new ServerMemory(Constants.Files.CONFIGS.resolve("viaflow").resolve("servers.json"),
            addon.logger());
    }

    @Subscribe
    public void onServerLogin(ServerLoginEvent event) {
        ConnectionHooks.prepare(null);
        this.attempt = null;
        this.currentServer = event.serverData().address();
        if (!this.addon.configuration().enabled().get()) {
            return;
        }

        ProtocolVersion target;
        if (this.addon.targetVersions().isAuto()) {
            if (!NativeVersion.get().isKnown()) {
                return;
            }

            ServerAddress address = event.serverData().address();
            AutoChoice choice = this.choices.get(address);
            boolean remembered = false;
            if (choice == null) {
                choice = this.memory.get(address);
                remembered = choice != null;
            }
            if (choice == null) {
                this.detectAndRetry(event);
                return;
            }
            this.attempt = new Attempt(event.serverData(), choice, remembered);
            target = choice.version();
        } else {
            target = this.addon.targetVersions().current();
        }
        if (target == null) {
            return;
        }

        if (!NativeVersion.get().isKnown()) {
            event.setDisconnectReason(Component.translatable("viaflow.connect.unsupportedClient",
                Component.text(Laby.labyAPI().minecraft().getVersion())));
            return;
        }

        // Waiting here would freeze the render thread, so a join while ViaVersion is still
        // starting is cancelled and repeated once it is ready.
        CompletableFuture<Void> startup = this.addon.startVia();
        if (!startup.isDone()) {
            event.setDisconnectReason(Component.translatable("viaflow.connect.starting"));
            this.retryWhenDone(event.serverData(), startup);
        } else if (startup.isCompletedExceptionally()) {
            event.setDisconnectReason(Component.translatable("viaflow.connect.failed"));
        } else {
            ConnectionHooks.prepare(target);
        }
    }

    @Subscribe
    public void onServerJoin(ServerJoinEvent event) {
        Attempt joined = this.attempt;
        this.attempt = null;
        if (joined != null) {
            this.memory.remember(joined.server().address(), joined.choice().version());
        }
    }

    /**
     * A server that claimed to accept this client's version but turns it away before the world
     * loads is joined again as the newest version it runs. A remembered version that is turned
     * away is forgotten, so the next join looks the server up again.
     */
    @Subscribe
    public void onServerKick(ServerKickEvent event) {
        Attempt kicked = this.attempt;
        this.attempt = null;
        if (kicked == null) {
            return;
        }

        ServerAddress address = kicked.server().address();
        ProtocolVersion fallback = kicked.choice().fallback();
        if (fallback != null) {
            this.choices.put(address, AutoChoice.fixed(fallback));
            event.setReason(Component.translatable("viaflow.connect.retrying", Component.text(fallback.getName())));
            this.retryWhenDone(kicked.server(), this.addon.startVia().thenCompose(ready -> afterLoginLimit()));
        } else if (kicked.remembered()) {
            this.memory.forget(address);
        }
    }

    /**
     * ViaVersion on a server behind a proxy names that server's own version, which the proxy's
     * answer to a ping doesn't. Joining as it from the next join spares a second translation on
     * the server.
     */
    @Subscribe
    public void onPayload(NetworkPayloadEvent event) {
        ServerAddress server = this.currentServer;
        ProtocolVersion translatedTo = ConnectionHooks.activeTarget();
        if (event.side() != NetworkPayloadEvent.Side.RECEIVE || server == null || translatedTo == null
            || !ServerDetails.CHANNEL.equals(event.identifier().toString())) {
            return;
        }

        int protocol = ServerDetails.protocol(event.getPayload());
        ProtocolVersion version = SelectableVersions.forServer(NativeVersion.get(), protocol);
        if (version != null && !version.equals(translatedTo)) {
            this.choices.put(server, AutoChoice.fixed(version));
            this.memory.remember(server, version);
        }
    }

    @Subscribe
    public void onServerDisconnect(ServerDisconnectEvent event) {
        ConnectionHooks.onDisconnect();
    }

    /**
     * Pinging would freeze the render thread as well, so the join is cancelled and repeated once
     * the server reported its version and ViaVersion is ready if that version needs it.
     */
    private void detectAndRetry(ServerLoginEvent event) {
        ConnectableServerData server = event.serverData();
        event.setDisconnectReason(Component.translatable("viaflow.connect.detecting"));
        CompletableFuture<Void> ready = ServerVersionDetector.detect(server.address()).thenCompose(choice -> {
            this.choices.put(server.address(), choice);
            return choice.version() == null ? CompletableFuture.<Void>completedFuture(null) : this.addon.startVia();
        });
        this.retryWhenDone(server, ready);
    }

    private void retryWhenDone(ConnectableServerData server, CompletableFuture<?> ready) {
        this.retryServer.set(server);
        ready.whenComplete((result, error) -> Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
            // A join started in the meantime replaces this one.
            if (this.retryServer.compareAndSet(server, null)) {
                server.connect();
            }
        }));
    }

    private static CompletableFuture<Void> afterLoginLimit() {
        return CompletableFuture.runAsync(() -> {
        }, CompletableFuture.delayedExecutor(LOGIN_LIMIT_MILLIS, TimeUnit.MILLISECONDS));
    }

    /**
     * A join in progress with the choice it was started with, and whether that choice was
     * remembered from an earlier game.
     */
    private record Attempt(ConnectableServerData server, AutoChoice choice, boolean remembered) {
    }
}
