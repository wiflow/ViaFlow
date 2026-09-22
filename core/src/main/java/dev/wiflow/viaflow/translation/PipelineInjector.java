package dev.wiflow.viaflow.translation;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.platform.ViaDecodeHandler;
import com.viaversion.viaversion.platform.ViaEncodeHandler;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.util.AttributeKey;
import java.util.List;

public final class PipelineInjector {

    public static final AttributeKey<ProtocolVersion> TARGET_VERSION =
        AttributeKey.valueOf("viaflow_target_version");

    private static final String DECODER = "decoder";
    private static final String UNCONFIGURED_DECODER = "inbound_config";
    private static final String ENCODER = "encoder";
    private static final String COMPRESS = "compress";
    private static final String DECOMPRESS = "decompress";
    private static final String READ_GATE = "viaflow-read-gate";

    private PipelineInjector() {
    }

    static void inject(Channel channel, ProtocolVersion target) {
        channel.attr(TARGET_VERSION).set(target);

        UserConnection connection = new UserConnectionImpl(channel, true);
        new ProtocolPipelineImpl(connection);

        // Minecraft 1.20.5 and newer start with an unconfigured decoder and pause reading after
        // packets that switch the protocol, which needs the read gate behind Via's decoder.
        ChannelPipeline pipeline = channel.pipeline();
        boolean unconfigured = pipeline.get(UNCONFIGURED_DECODER) != null;
        pipeline.addBefore(unconfigured ? UNCONFIGURED_DECODER : DECODER, ViaDecodeHandler.NAME,
            new ViaDecodeHandler(connection));
        pipeline.addBefore(ENCODER, ViaEncodeHandler.NAME, new ViaEncodeHandler(connection));
        if (unconfigured) {
            pipeline.addAfter(ViaDecodeHandler.NAME, READ_GATE, ReadGate.create());
        }
    }

    /**
     * Minecraft adds its compression handlers next to its own codec, which can leave them on the
     * wrong side of Via's handlers. Moves Via's handlers back between compression and the codec.
     * Adapted from ViaVersion's ViaChannelInitializer#reorderPipeline.
     */
    static void reorderForCompression(Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        List<String> names = pipeline.names();
        int decompressIndex = names.indexOf(DECOMPRESS);
        int viaDecoderIndex = names.indexOf(ViaDecodeHandler.NAME);
        if (decompressIndex == -1 || viaDecoderIndex == -1 || decompressIndex < viaDecoderIndex) {
            return;
        }

        ChannelHandler encoder = pipeline.remove(ViaEncodeHandler.NAME);
        ChannelHandler decoder = pipeline.remove(ViaDecodeHandler.NAME);
        pipeline.addAfter(COMPRESS, ViaEncodeHandler.NAME, encoder);
        pipeline.addAfter(DECOMPRESS, ViaDecodeHandler.NAME, decoder);
        if (pipeline.get(READ_GATE) != null) {
            pipeline.addAfter(ViaDecodeHandler.NAME, READ_GATE, pipeline.remove(READ_GATE));
        }
    }
}
