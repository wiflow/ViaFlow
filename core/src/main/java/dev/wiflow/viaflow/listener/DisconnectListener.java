package dev.wiflow.viaflow.listener;

import dev.wiflow.viaflow.ViaFlowCommon;
import dev.wiflow.viaflow.util.VersionTracker;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;

public class DisconnectListener {

    @Subscribe
    public void onDisconnect(ServerDisconnectEvent event) {
        VersionTracker.clear();
        if (ViaFlowCommon.getInstance() != null) {
            ViaFlowCommon.getInstance().onDisconnect();
        }
    }
}
