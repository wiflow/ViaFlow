package dev.wiflow.viaflow.v26_2.mixins;

import dev.wiflow.viaflow.translation.ConnectionHooks;
import io.netty.channel.Channel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.Connection$1")
public abstract class MixinConnectionInit {

    @Inject(method = "initChannel", at = @At("RETURN"), remap = false)
    private void viaflow$injectVia(Channel channel, CallbackInfo ci) {
        ConnectionHooks.onChannelInit(channel);
    }
}
