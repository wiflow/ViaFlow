package dev.wiflow.viaflow.v1_12_2.mixins;

import dev.wiflow.viaflow.translation.ConnectionHooks;
import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public abstract class MixinConnection {

    @Shadow
    private Channel channel;

    @Inject(method = "setCompressionThreshold", at = @At("RETURN"))
    private void viaflow$reorderPipeline(CallbackInfo ci) {
        ConnectionHooks.onCompression(this.channel);
    }
}
