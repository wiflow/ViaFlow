package dev.wiflow.viaflow.v1_17_1.mixins;

import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.ViaFlowCommon;
import dev.wiflow.viaflow.ViaFlowConfiguration;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class MixinJoinMultiplayerScreen extends Screen {

    @Unique
    private Button viaFlowButton;

    protected MixinJoinMultiplayerScreen(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void viaflow$addVersionButton(CallbackInfo ci) {
        if (ViaFlowAddon.getInstance() == null || !ViaFlowAddon.getInstance().configuration().enabled().get()) {
            return;
        }

        this.viaFlowButton = new Button(
            this.width - 105,
            5,
            100,
            20,
            new TextComponent(viaflow$getButtonText()),
            button -> {
                viaflow$cycleVersion();
                button.setMessage(new TextComponent(viaflow$getButtonText()));
            }
        );
        this.addRenderableWidget(this.viaFlowButton);
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
