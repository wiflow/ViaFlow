package dev.wiflow.viaflow.v1_12_2.mixins;

import dev.wiflow.viaflow.translation.ConnectionHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {

    @Shadow
    private boolean doneLoadingTerrain;

    @Shadow
    private Minecraft client;

    /**
     * Proxies move players on 1.20.2 and newer to another server with a second join packet. This
     * client only creates a player on the first one and would keep the old player, whose world is
     * still the previous server's, so it would fall through the new world's blocks. Proxies used to
     * follow a join with a respawn that recreated the player; ViaBackwards sends the join alone, so
     * the old player is dropped here and loadWorld creates one in the new world.
     */
    @Inject(
        method = "handleJoinGame",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;)V")
    )
    private void viaflow$recreatePlayerAfterSwitch(CallbackInfo ci) {
        if (ConnectionHooks.activeTarget() != null) {
            this.client.player = null;
        }
    }

    /**
     * This client shows the terrain screen for every join but hides it only after the first one,
     * so the flag is re-armed for the next position packet to hide it.
     */
    @Inject(method = "handleJoinGame", at = @At("RETURN"))
    private void viaflow$hideTerrainScreenAfterSwitch(CallbackInfo ci) {
        if (ConnectionHooks.activeTarget() != null) {
            this.doneLoadingTerrain = false;
        }
    }
}
