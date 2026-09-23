package dev.wiflow.viaflow;

import dev.wiflow.viaflow.version.TargetVersions;
import dev.wiflow.viaflow.version.VersionEntries;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class ViaFlowConfiguration extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @DropdownSetting(entries = VersionEntries.class)
    private final ConfigProperty<String> targetVersion = new ConfigProperty<>(TargetVersions.AUTO);

    @Override
    public ConfigProperty<Boolean> enabled() {
        return this.enabled;
    }

    public ConfigProperty<String> targetVersion() {
        return this.targetVersion;
    }
}
