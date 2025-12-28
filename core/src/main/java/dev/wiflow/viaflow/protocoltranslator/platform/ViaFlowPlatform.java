package dev.wiflow.viaflow.protocoltranslator.platform;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.vialoader.impl.platform.ViaVersionPlatformImpl;
import dev.wiflow.viaflow.ViaFlowCommon;
import java.io.File;
import java.util.UUID;

public class ViaFlowPlatform extends ViaVersionPlatformImpl {

    private final ProtocolVersion nativeVersion;

    public ViaFlowPlatform(File configDir, ProtocolVersion nativeVersion) {
        super(configDir);
        this.nativeVersion = nativeVersion;
    }

    @Override
    public String getPlatformName() {
        return "ViaFlow";
    }

    @Override
    public String getPlatformVersion() {
        return "1.0.0";
    }

    @Override
    public boolean isProxy() {
        return false;
    }

    @Override
    public JsonObject getDump() {
        JsonObject dump = new JsonObject();
        dump.addProperty("platform", "ViaFlow");
        dump.addProperty("platform_version", getPlatformVersion());
        dump.addProperty("loader", "LabyMod 4");
        dump.addProperty("native_version", nativeVersion.getName());
        if (ViaFlowCommon.getInstance() != null) {
            dump.addProperty("target_version", ViaFlowCommon.getInstance().getTargetVersion().getName());
        }
        return dump;
    }

    public void sendMessage(UUID uuid, String message) {
        getLogger().info(message);
    }

    public boolean kickPlayer(UUID uuid, String message) {
        return false;
    }

    public boolean disconnect(UserConnection connection, String message) {
        return false;
    }

    public ProtocolVersion getNativeVersion() {
        return nativeVersion;
    }
}
