package dev.wiflow.viaflow.translation;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.flow.FlowControlHandler;

/**
 * Holds back packets Via decoded while Minecraft paused reading, so they reach the codec only
 * after it switched protocols. Extends a Netty 4.1 class: create it only on 1.20.5 and newer.
 */
final class ReadGate extends FlowControlHandler {

    static ChannelHandler create() {
        return new ReadGate();
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().config().isAutoRead()) {
            super.read(ctx);
        }
    }
}
