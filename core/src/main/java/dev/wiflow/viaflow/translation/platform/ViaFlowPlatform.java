package dev.wiflow.viaflow.translation.platform;

import com.viaversion.viaversion.configuration.AbstractViaConfig;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.platform.UserConnectionViaVersionPlatform;
import dev.wiflow.viaflow.version.NativeVersion;
import java.io.File;
import java.util.logging.Logger;

public final class ViaFlowPlatform extends UserConnectionViaVersionPlatform {

    private final String version;

    public ViaFlowPlatform(File dataFolder, String version) {
        super(dataFolder);
        this.version = version;
    }

    @Override
    public Logger createLogger(String name) {
        return new LabyLoggerBridge(name);
    }

    @Override
    protected AbstractViaConfig createConfig() {
        return new ViaFlowViaConfig(new File(this.getDataFolder(), "viaversion.yml"), this.getLogger());
    }

    @Override
    public String getPlatformName() {
        return "ViaFlow";
    }

    @Override
    public String getPlatformVersion() {
        return this.version;
    }

    @Override
    public JsonObject getDump() {
        JsonObject dump = new JsonObject();
        dump.addProperty("native_version", NativeVersion.get().getName());
        return dump;
    }
}
