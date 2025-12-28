package dev.wiflow.viaflow.protocoltranslator.netty;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public interface ViaFlowNetworkManager {

    ProtocolVersion viaFlow$getTargetVersion();

    void viaFlow$setTargetVersion(ProtocolVersion version);

    default void viaFlow$setupPreNettyDecryption() {
    }
}
