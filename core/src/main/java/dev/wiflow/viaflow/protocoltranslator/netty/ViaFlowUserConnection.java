package dev.wiflow.viaflow.protocoltranslator.netty;

import com.viaversion.viaversion.connection.UserConnectionImpl;
import io.netty.channel.Channel;

public class ViaFlowUserConnection extends UserConnectionImpl {

    private boolean compressionEnabled = false;

    public ViaFlowUserConnection(Channel channel, boolean clientSide) {
        super(channel, clientSide);
    }

    public boolean isCompressionEnabled() {
        return compressionEnabled;
    }

    public void setCompressionEnabled(boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }
}
