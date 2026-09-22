package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.labymod.api.configuration.loader.property.ConfigProperty;

/**
 * The server version to connect as, stored in the settings as a ViaVersion version name.
 */
public final class TargetVersions {

    public static final String NATIVE = "native";

    private final ConfigProperty<String> setting;

    public TargetVersions(ConfigProperty<String> setting) {
        this.setting = setting;
    }

    /**
     * Returns the version to translate to, or null when connecting natively.
     */
    public ProtocolVersion current() {
        String name = this.setting.get();
        if (name == null || NATIVE.equals(name)) {
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
