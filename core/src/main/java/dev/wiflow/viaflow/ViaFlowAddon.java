package dev.wiflow.viaflow;

import dev.wiflow.viaflow.commands.ViaFlowCommand;
import dev.wiflow.viaflow.listener.DisconnectListener;
import dev.wiflow.viaflow.widgets.ViaFlowHudWidget;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class ViaFlowAddon extends LabyAddon<ViaFlowConfiguration> {

    private static ViaFlowAddon instance;

    @Override
    protected void enable() {
        instance = this;

        // Reset target version to NATIVE on every startup
        this.configuration().targetVersion().set(ViaFlowConfiguration.ProtocolVersionSetting.NATIVE);
        ViaFlowCommon.init(this);
        this.registerSettingCategory();
        this.labyAPI().commandService().register(new ViaFlowCommand(this));
        this.labyAPI().hudWidgetRegistry().register(new ViaFlowHudWidget());
        this.registerListener(new DisconnectListener());
        // Version selector button is added via MixinJoinMultiplayerScreen
    }

    @Override
    protected Class<ViaFlowConfiguration> configurationClass() {
        return ViaFlowConfiguration.class;
    }

    public static ViaFlowAddon getInstance() {
        return instance;
    }
}
