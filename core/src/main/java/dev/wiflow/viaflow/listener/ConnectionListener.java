package dev.wiflow.viaflow.listener;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.translation.ConnectionHooks;
import dev.wiflow.viaflow.version.NativeVersion;
import java.util.concurrent.CompletableFuture;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerLoginEvent;

public class ConnectionListener {

    private final ViaFlowAddon addon;

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

        // Connecting blocks the render thread, so a join while ViaVersion is still starting is
        // refused instead of waiting for it.
        CompletableFuture<Void> startup = this.addon.startVia();
        if (!startup.isDone()) {
            event.setDisconnectReason(Component.translatable("viaflow.connect.starting"));
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
}
