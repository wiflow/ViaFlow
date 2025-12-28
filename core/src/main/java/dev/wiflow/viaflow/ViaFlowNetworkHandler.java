package dev.wiflow.viaflow;

import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface ViaFlowNetworkHandler {

    int getNativeProtocolVersion();

    String getMinecraftVersion();

    boolean isConnected();

    void disconnect(String reason);
}
