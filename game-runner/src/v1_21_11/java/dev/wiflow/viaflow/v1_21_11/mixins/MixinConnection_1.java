package dev.wiflow.viaflow.v1_21_11.mixins;

import dev.wiflow.viaflow.ViaFlowCommon;
import dev.wiflow.viaflow.protocoltranslator.netty.ViaFlowNetworkManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.Connection$1")
public class MixinConnection_1 {

    @Inject(method = "initChannel", at = @At("TAIL"), remap = false)
    private void hookViaPipeline(Channel channel, CallbackInfo ci) {
        final ChannelHandler connection = channel.pipeline().get("packet_handler");
        ViaFlowCommon.getInstance().inject(channel, (ViaFlowNetworkManager) connection);
    }
}
