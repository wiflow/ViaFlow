package dev.wiflow.viaflow.v1_12_2.mixins;

import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.ViaFlowCommon;
import dev.wiflow.viaflow.ViaFlowConfiguration;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer extends GuiScreen {

    @Unique
    private static final int VIAFLOW_BUTTON_ID = 1_000_000;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void viaflow$addVersionButton(CallbackInfo ci) {
        if (ViaFlowAddon.getInstance() == null || !ViaFlowAddon.getInstance().configuration().enabled().get()) {
            return;
        }

        this.buttonList.add(new GuiButton(
            VIAFLOW_BUTTON_ID,
            this.width - 105,
            5,
            100,
            20,
            viaflow$getButtonText()
        ));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void viaflow$handleButtonClick(GuiButton button, CallbackInfo ci) {
        if (button.id == VIAFLOW_BUTTON_ID) {
            viaflow$cycleVersion();
            button.displayString = viaflow$getButtonText();
        }
    }

    @Unique
    private String viaflow$getButtonText() {
        if (ViaFlowCommon.getInstance() != null) {
            return "ViaFlow: " + ViaFlowCommon.getInstance().getTargetVersion().getName();
        }
        return "ViaFlow";
    }

    @Unique
    private void viaflow$cycleVersion() {
        ViaFlowAddon addon = ViaFlowAddon.getInstance();
        if (addon == null) return;

        ViaFlowConfiguration.ProtocolVersionSetting current = addon.configuration().targetVersion().get();
        addon.configuration().targetVersion().set(current.next());
    }
}
