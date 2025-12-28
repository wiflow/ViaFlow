package dev.wiflow.viaflow.v1_8_9.mixins;

import dev.wiflow.viaflow.ViaFlowCommon;
import dev.wiflow.viaflow.protocoltranslator.netty.ViaFlowNetworkManager;
import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.NetworkManager$5")
public class MixinNetworkManager_5 {

    @Final
    @Mutable
    NetworkManager val$networkmanager;

    @Inject(method = "initChannel", at = @At("TAIL"), remap = false)
    private void hookViaPipeline(Channel channel, CallbackInfo ci) {
        ViaFlowCommon.getInstance().inject(channel, (ViaFlowNetworkManager) val$networkmanager);
    }
}
