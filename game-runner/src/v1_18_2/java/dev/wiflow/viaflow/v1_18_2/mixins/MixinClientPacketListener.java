package dev.wiflow.viaflow.v1_18_2.mixins;

import dev.wiflow.viaflow.translation.ConnectionHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener {

    @Shadow
    @Final
    private Minecraft minecraft;

    /**
     * Proxies move players on 1.20.2 and newer to another server with a second join packet. This
     * client only creates a player on the first one and would keep the old player, whose level is
     * still the previous server's, so it would fall through the new level's blocks. Proxies used
     * to follow a join with a respawn that recreated the player; ViaBackwards sends the join alone,
     * so the old player is dropped here, right before the client checks for one.
     */
    @Inject(
        method = "handleLogin",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;setLevel(Lnet/minecraft/client/multiplayer/ClientLevel;)V",
            shift = At.Shift.AFTER
        )
    )
    private void viaflow$recreatePlayerAfterSwitch(CallbackInfo ci) {
        if (ConnectionHooks.activeTarget() != null) {
            this.minecraft.player = null;
        }
    }
}
