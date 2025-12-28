package dev.wiflow.viaflow.v1_8_9.mixins;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.wiflow.viaflow.ViaFlowCommon;
import dev.wiflow.viaflow.protocoltranslator.netty.ViaFlowNetworkManager;
import io.netty.channel.Channel;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager implements ViaFlowNetworkManager {

    @Shadow
    private Channel channel;

    @Unique
    private ProtocolVersion viaFlow$targetVersion;

    @Inject(method = "setCompressionTreshold", at = @At("RETURN"))
    public void reorderPipeline(int threshold, CallbackInfo ci) {
        ViaFlowCommon.getInstance().reorderCompression(channel);
    }

    @Override
    public ProtocolVersion viaFlow$getTargetVersion() {
        return viaFlow$targetVersion;
    }

    @Override
    public void viaFlow$setTargetVersion(ProtocolVersion version) {
        this.viaFlow$targetVersion = version;
    }
}
