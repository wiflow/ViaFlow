package dev.wiflow.viaflow.protocoltranslator.netty;

import com.viaversion.vialoader.netty.VLLegacyPipeline;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.channel.ChannelHandlerContext;

public class ViaFlowPipeline extends VLLegacyPipeline {

    private String actualDecoderName = "decoder";

    public ViaFlowPipeline(UserConnection user, ProtocolVersion version) {
        super(user, version);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        // Detect the correct decoder name for this MC version
        if (ctx.pipeline().get("decoder") != null) {
            actualDecoderName = "decoder";
        } else if (ctx.pipeline().get("inbound_config") != null) {
            actualDecoderName = "inbound_config";
        }
        super.handlerAdded(ctx);
    }

    @Override
    protected String decompressName() {
        return "decompress";
    }

    @Override
    protected String compressName() {
        return "compress";
    }

    @Override
    protected String packetDecoderName() {
        return actualDecoderName;
    }

    @Override
    protected String packetEncoderName() {
        return "encoder";
    }

    @Override
    protected String lengthSplitterName() {
        return "splitter";
    }

    @Override
    protected String lengthPrependerName() {
        return "prepender";
    }
}
