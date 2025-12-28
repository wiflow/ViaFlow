package dev.wiflow.viaflow.protocoltranslator.provider;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;
import dev.wiflow.viaflow.ViaFlowCommon;
import dev.wiflow.viaflow.protocoltranslator.netty.ViaFlowNetworkManager;

public class ViaFlowVersionProvider extends BaseVersionProvider {

    @Override
    public ProtocolVersion getClosestServerProtocol(UserConnection connection) throws Exception {
        if (connection.isClientSide()) {
            ViaFlowNetworkManager networkManager = connection.getChannel()
                .attr(ViaFlowCommon.VF_NETWORK_MANAGER).get();

            if (networkManager != null) {
                ProtocolVersion trackedVersion = networkManager.viaFlow$getTargetVersion();
                if (trackedVersion != null) {
                    return trackedVersion;
                }
            }

            return ViaFlowCommon.getInstance().getTargetVersion();
        }

        return super.getClosestServerProtocol(connection);
    }
}
