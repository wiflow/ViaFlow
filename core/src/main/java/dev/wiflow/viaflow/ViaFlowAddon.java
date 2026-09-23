package dev.wiflow.viaflow;

import dev.wiflow.viaflow.commands.ViaFlowCommand;
import dev.wiflow.viaflow.listener.ConnectionListener;
import dev.wiflow.viaflow.listener.ServerListListener;
import dev.wiflow.viaflow.translation.ViaRuntime;
import dev.wiflow.viaflow.version.TargetVersions;
import dev.wiflow.viaflow.widgets.ViaFlowHudWidget;
import java.util.concurrent.CompletableFuture;
import net.labymod.api.Constants;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class ViaFlowAddon extends LabyAddon<ViaFlowConfiguration> {

    private TargetVersions targetVersions;

    @Override
    protected void enable() {
        // Every session starts on auto, so ViaVersion only loads once a server needs another
        // version or one is picked.
        ViaFlowConfiguration configuration = this.configuration();
        configuration.targetVersion().set(TargetVersions.AUTO);
        this.targetVersions = new TargetVersions(configuration.targetVersion());
        configuration.targetVersion().addChangeListener(() -> {
            if (this.targetVersions.current() != null) {
                this.startVia();
            }
        });

        this.registerSettingCategory();
        this.registerCommand(new ViaFlowCommand(this));
        this.registerListener(new ConnectionListener(this));
        this.registerListener(new ServerListListener(this));
        this.labyAPI().hudWidgetRegistry().register(new ViaFlowHudWidget());
    }

    @Override
    protected Class<ViaFlowConfiguration> configurationClass() {
        return ViaFlowConfiguration.class;
    }

    public TargetVersions targetVersions() {
        return this.targetVersions;
    }

    public CompletableFuture<Void> startVia() {
        return ViaRuntime.start(
            Constants.Files.CONFIGS.resolve("viaflow").toFile(),
            this.addonInfo().getVersion(),
            this.logger()
        );
    }
}
