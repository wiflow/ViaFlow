package dev.wiflow.viaflow;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
public class ViaFlowConfiguration extends AddonConfig {

    @SwitchSetting
    @SpriteSlot(size = 32)
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @DropdownSetting
    @SpriteSlot(size = 32, x = 1)
    private final ConfigProperty<ProtocolVersionSetting> targetVersion =
        new ConfigProperty<>(ProtocolVersionSetting.NATIVE);

    @Override
    public ConfigProperty<Boolean> enabled() {
        return this.enabled;
    }

    public ConfigProperty<ProtocolVersionSetting> targetVersion() {
        return this.targetVersion;
    }

    public enum ProtocolVersionSetting {
        NATIVE("Native"),
        V1_21_11("1.21.11"),
        V1_21_10("1.21.10"),
        V1_21_8("1.21.8"),
        V1_21_5("1.21.5"),
        V1_21_4("1.21.4"),
        V1_21_2("1.21.2"),
        V1_21("1.21"),
        V1_20_5("1.20.5"),
        V1_20_3("1.20.3"),
        V1_20_2("1.20.2"),
        V1_20("1.20"),
        V1_19_4("1.19.4"),
        V1_19_3("1.19.3"),
        V1_19_1("1.19.1"),
        V1_19("1.19"),
        V1_18_2("1.18.2"),
        V1_18("1.18"),
        V1_17_1("1.17.1"),
        V1_17("1.17"),
        V1_16_4("1.16.4"),
        V1_16_3("1.16.3"),
        V1_16_2("1.16.2"),
        V1_16_1("1.16.1"),
        V1_16("1.16"),
        V1_15_2("1.15.2"),
        V1_15_1("1.15.1"),
        V1_15("1.15"),
        V1_14_4("1.14.4"),
        V1_14("1.14"),
        V1_13_2("1.13.2"),
        V1_13("1.13"),
        V1_12_2("1.12.2"),
        V1_12("1.12"),
        V1_11("1.11"),
        V1_10("1.10"),
        V1_9_4("1.9.4"),
        V1_9("1.9"),
        V1_8("1.8");

        private final String displayName;

        ProtocolVersionSetting(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public ProtocolVersion toProtocolVersion() {
            return switch (this) {
                case NATIVE -> null;
                case V1_21_11 -> ProtocolVersion.getClosest("1.21.11");
                case V1_21_10 -> ProtocolVersion.getClosest("1.21.10");
                case V1_21_8 -> ProtocolVersion.getClosest("1.21.8");
                case V1_21_5 -> ProtocolVersion.getClosest("1.21.5");
                case V1_21_4 -> ProtocolVersion.v1_21_4;
                case V1_21_2 -> ProtocolVersion.v1_21_2;
                case V1_21 -> ProtocolVersion.v1_21;
                case V1_20_5 -> ProtocolVersion.v1_20_5;
                case V1_20_3 -> ProtocolVersion.v1_20_3;
                case V1_20_2 -> ProtocolVersion.v1_20_2;
                case V1_20 -> ProtocolVersion.v1_20;
                case V1_19_4 -> ProtocolVersion.v1_19_4;
                case V1_19_3 -> ProtocolVersion.v1_19_3;
                case V1_19_1 -> ProtocolVersion.v1_19_1;
                case V1_19 -> ProtocolVersion.v1_19;
                case V1_18_2 -> ProtocolVersion.v1_18_2;
                case V1_18 -> ProtocolVersion.v1_18;
                case V1_17_1 -> ProtocolVersion.v1_17_1;
                case V1_17 -> ProtocolVersion.v1_17;
                case V1_16_4 -> ProtocolVersion.v1_16_4;
                case V1_16_3 -> ProtocolVersion.v1_16_3;
                case V1_16_2 -> ProtocolVersion.v1_16_2;
                case V1_16_1 -> ProtocolVersion.v1_16_1;
                case V1_16 -> ProtocolVersion.v1_16;
                case V1_15_2 -> ProtocolVersion.v1_15_2;
                case V1_15_1 -> ProtocolVersion.v1_15_1;
                case V1_15 -> ProtocolVersion.v1_15;
                case V1_14_4 -> ProtocolVersion.v1_14_4;
                case V1_14 -> ProtocolVersion.v1_14;
                case V1_13_2 -> ProtocolVersion.v1_13_2;
                case V1_13 -> ProtocolVersion.v1_13;
                case V1_12_2 -> ProtocolVersion.v1_12_2;
                case V1_12 -> ProtocolVersion.v1_12;
                case V1_11 -> ProtocolVersion.v1_11;
                case V1_10 -> ProtocolVersion.v1_10;
                case V1_9_4 -> ProtocolVersion.v1_9_3;
                case V1_9 -> ProtocolVersion.v1_9;
                case V1_8 -> ProtocolVersion.v1_8;
            };
        }

        @Override
        public String toString() {
            return displayName;
        }

        public ProtocolVersionSetting next() {
            ProtocolVersionSetting[] values = values();
            int nextIndex = (this.ordinal() + 1) % values.length;
            return values[nextIndex];
        }
    }
}
