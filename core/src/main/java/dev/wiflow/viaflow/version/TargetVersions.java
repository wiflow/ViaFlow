package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.labymod.api.configuration.loader.property.ConfigProperty;

/**
 * The server version to connect as, stored in the settings as a ViaVersion version name, as
 * {@link #AUTO} to use the version each server reports, or as {@link #NATIVE}.
 */
public final class TargetVersions {

    public static final String AUTO = "auto";
    public static final String NATIVE = "native";

    private final ConfigProperty<String> setting;

    public TargetVersions(ConfigProperty<String> setting) {
        this.setting = setting;
    }

    public boolean isAuto() {
        return AUTO.equals(this.setting.get());
    }

    public void setAuto() {
        this.setting.set(AUTO);
    }

    /**
     * Returns the picked version to translate to, or null when connecting natively or on auto.
     */
    public ProtocolVersion current() {
        String name = this.setting.get();
        if (name == null || NATIVE.equals(name) || AUTO.equals(name)) {
            return null;
        }

        ProtocolVersion version = SelectableVersions.find(name);
        return version == null || version.equals(NativeVersion.get()) ? null : version;
    }

    /**
     * Selects a version to connect as. Null or the native version connects natively.
     */
    public void set(ProtocolVersion version) {
        boolean natively = version == null || version.equals(NativeVersion.get());
        this.setting.set(natively ? NATIVE : version.getName());
    }
}
