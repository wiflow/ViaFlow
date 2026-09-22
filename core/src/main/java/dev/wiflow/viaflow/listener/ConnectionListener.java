package dev.wiflow.viaflow.listener;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.translation.ConnectionHooks;
import dev.wiflow.viaflow.version.NativeVersion;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.server.ConnectableServerData;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;

public class ConnectionListener {

    private final ViaFlowAddon addon;
    private final AtomicReference<ConnectableServerData> retryServer = new AtomicReference<>();

    public ConnectionListener(ViaFlowAddon addon) {
        this.addon = addon;
    }

    @Subscribe
    public void onServerLogin(ServerLoginEvent event) {
        ConnectionHooks.prepare(null);
        if (!this.addon.configuration().enabled().get()) {
            return;
        }

        ProtocolVersion target = this.addon.targetVersions().current();
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
            this.retryServer.set(event.serverData());
            startup.thenRun(() -> Laby.labyAPI().minecraft().executeOnRenderThread(this::retryJoin));
        } else if (startup.isCompletedExceptionally()) {
            event.setDisconnectReason(Component.translatable("viaflow.connect.failed"));
        } else {
            ConnectionHooks.prepare(target);
        }
    }

    @Subscribe
    public void onServerDisconnect(ServerDisconnectEvent event) {
        ConnectionHooks.onDisconnect();
    }

    private void retryJoin() {
        ConnectableServerData server = this.retryServer.getAndSet(null);
        if (server != null) {
            server.connect();
        }
    }
}
