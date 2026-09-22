package dev.wiflow.viaflow.translation.platform;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.version.BaseVersionProvider;
import dev.wiflow.viaflow.translation.PipelineInjector;

/**
 * Tells ViaVersion which server version a client connection talks to, as chosen when the
 * connection was set up.
 */
final class ChannelVersionProvider extends BaseVersionProvider {

    @Override
    public ProtocolVersion getClosestServerProtocol(UserConnection connection) throws Exception {
        if (connection.isClientSide()) {
            ProtocolVersion target = connection.getChannel().attr(PipelineInjector.TARGET_VERSION).get();
            if (target != null) {
                return target;
            }
        }
        return super.getClosestServerProtocol(connection);
    }
}
