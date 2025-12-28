package dev.wiflow.viaflow;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import dev.wiflow.viaflow.protocoltranslator.ViaFlowLoader;
import dev.wiflow.viaflow.protocoltranslator.ViaFlowInjector;
import dev.wiflow.viaflow.protocoltranslator.netty.ViaFlowNetworkManager;
import dev.wiflow.viaflow.protocoltranslator.netty.ViaFlowPipeline;
import dev.wiflow.viaflow.protocoltranslator.netty.ViaFlowUserConnection;
import dev.wiflow.viaflow.protocoltranslator.platform.ViaFlowPlatform;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.labymod.api.Laby;
import com.viaversion.vialoader.ViaLoader;
import com.viaversion.vialoader.impl.platform.ViaBackwardsPlatformImpl;
import com.viaversion.vialoader.impl.platform.ViaLegacyPlatformImpl;
import com.viaversion.vialoader.impl.platform.ViaRewindPlatformImpl;
import com.viaversion.vialoader.netty.CompressionReorderEvent;
import java.io.File;
import java.util.logging.Logger;

public class ViaFlowCommon {

    public static final AttributeKey<UserConnection> VF_USER_CONNECTION =
        AttributeKey.valueOf("viaflow_user_connection");
    public static final AttributeKey<ProtocolVersion> VF_TARGET_VERSION =
        AttributeKey.valueOf("viaflow_target_version");
    public static final AttributeKey<ViaFlowNetworkManager> VF_NETWORK_MANAGER =
        AttributeKey.valueOf("viaflow_network_manager");

    private static ViaFlowCommon instance;
    private static ProtocolVersion nativeVersion;
    private static boolean initialized = false;

    private final ViaFlowAddon addon;
    private final Logger logger;
    private final File configDir;

    private ProtocolVersion targetVersion;
    private UserConnection currentConnection;
    private boolean serverHasViaVersion = false;
    private String detectedServerBrand = null;

    private ViaFlowCommon(ViaFlowAddon addon) {
        this.addon = addon;
        this.logger = Logger.getLogger("ViaFlow");
        this.configDir = new File(addon.labyAPI().labyModLoader().getGameDirectory().toFile(), "ViaFlow");

        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }

    public static void init(ViaFlowAddon addon) {
        if (initialized) return;

        try {
            nativeVersion = detectNativeVersion(addon);
            instance = new ViaFlowCommon(addon);
            instance.targetVersion = nativeVersion;

            addon.logger().info("Native version: " + nativeVersion.getName());

            ViaLoader.init(
                new ViaFlowPlatform(instance.configDir, nativeVersion),
                new ViaFlowLoader(),
                new ViaFlowInjector(),
                null,
                ViaBackwardsPlatformImpl::new,
                ViaRewindPlatformImpl::new,
                ViaLegacyPlatformImpl::new
            );

            initialized = true;
            addon.logger().info("ViaFlow ready: " +
                Via.getManager().getProtocolManager().getSupportedVersions().first().getName() +
                " - " +
                Via.getManager().getProtocolManager().getSupportedVersions().last().getName());

        } catch (Exception e) {
            addon.logger().error("Failed to initialize ViaFlow", e);
        }
    }

    private static ProtocolVersion detectNativeVersion(ViaFlowAddon addon) {
        String mcVersion = Laby.labyAPI().minecraft().getVersion();
        return switch (mcVersion) {
            case "1.21.5" -> ProtocolVersion.v1_21_4;
            case "1.21.4" -> ProtocolVersion.v1_21_4;
            case "1.21.3", "1.21.2" -> ProtocolVersion.v1_21_2;
            case "1.21.1", "1.21" -> ProtocolVersion.v1_21;
            case "1.20.6", "1.20.5" -> ProtocolVersion.v1_20_5;
            case "1.20.4", "1.20.3" -> ProtocolVersion.v1_20_3;
            case "1.20.2" -> ProtocolVersion.v1_20_2;
            case "1.20.1", "1.20" -> ProtocolVersion.v1_20;
            case "1.19.4" -> ProtocolVersion.v1_19_4;
            case "1.19.3" -> ProtocolVersion.v1_19_3;
            case "1.19.2", "1.19.1" -> ProtocolVersion.v1_19_1;
            case "1.19" -> ProtocolVersion.v1_19;
            case "1.18.2" -> ProtocolVersion.v1_18_2;
            case "1.18.1", "1.18" -> ProtocolVersion.v1_18;
            case "1.17.1" -> ProtocolVersion.v1_17_1;
            case "1.17" -> ProtocolVersion.v1_17;
            case "1.16.5", "1.16.4" -> ProtocolVersion.v1_16_4;
            case "1.16.3" -> ProtocolVersion.v1_16_3;
            case "1.16.2" -> ProtocolVersion.v1_16_2;
            case "1.16.1" -> ProtocolVersion.v1_16_1;
            case "1.16" -> ProtocolVersion.v1_16;
            case "1.15.2" -> ProtocolVersion.v1_15_2;
            case "1.15.1" -> ProtocolVersion.v1_15_1;
            case "1.15" -> ProtocolVersion.v1_15;
            case "1.14.4" -> ProtocolVersion.v1_14_4;
            case "1.14.3" -> ProtocolVersion.v1_14_3;
            case "1.14.2" -> ProtocolVersion.v1_14_2;
            case "1.14.1" -> ProtocolVersion.v1_14_1;
            case "1.14" -> ProtocolVersion.v1_14;
            case "1.13.2" -> ProtocolVersion.v1_13_2;
            case "1.13.1" -> ProtocolVersion.v1_13_1;
            case "1.13" -> ProtocolVersion.v1_13;
            case "1.12.2" -> ProtocolVersion.v1_12_2;
            case "1.12.1" -> ProtocolVersion.v1_12_1;
            case "1.12" -> ProtocolVersion.v1_12;
            case "1.11.2", "1.11.1" -> ProtocolVersion.v1_11_1;
            case "1.11" -> ProtocolVersion.v1_11;
            case "1.10.2", "1.10.1", "1.10" -> ProtocolVersion.v1_10;
            case "1.9.4", "1.9.3" -> ProtocolVersion.v1_9_3;
            case "1.9.2" -> ProtocolVersion.v1_9_2;
            case "1.9.1" -> ProtocolVersion.v1_9_1;
            case "1.9" -> ProtocolVersion.v1_9;
            case "1.8.9", "1.8.8", "1.8" -> ProtocolVersion.v1_8;
            default -> ProtocolVersion.v1_21_4;
        };
    }

    public void inject(Channel channel, ViaFlowNetworkManager networkManager) {
        if (!initialized || !addon.configuration().enabled().get()) return;

        ProtocolVersion target = networkManager != null ? networkManager.viaFlow$getTargetVersion() : null;
        if (target == null) target = getTargetVersion();

        if (networkManager != null && networkManager.viaFlow$getTargetVersion() == null) {
            networkManager.viaFlow$setTargetVersion(target);
        }

        if (target.equals(nativeVersion)) return;

        try {
            if (networkManager != null) {
                channel.attr(VF_NETWORK_MANAGER).set(networkManager);
            }

            UserConnection user = new ViaFlowUserConnection(channel, true);
            new ProtocolPipelineImpl(user);

            channel.attr(VF_USER_CONNECTION).set(user);
            channel.attr(VF_TARGET_VERSION).set(target);
            this.currentConnection = user;

            // Debug: print pipeline handlers
            addon.logger().info("Pipeline handlers: " + channel.pipeline().names());

            // Check which decoder name exists in the pipeline
            String decoderName = null;
            if (channel.pipeline().get("decoder") != null) {
                decoderName = "decoder";
            } else if (channel.pipeline().get("inbound_config") != null) {
                decoderName = "inbound_config";
            }

            addon.logger().info("Using decoder name: " + decoderName);

            if (decoderName != null) {
                channel.pipeline().addLast(new ViaFlowPipeline(user, target));
            } else {
                addon.logger().warn("No decoder found in pipeline, skipping injection");
            }

        } catch (Exception e) {
            addon.logger().error("Pipeline injection failed", e);
        }
    }

    public void reorderCompression(Channel channel) {
        if (channel != null && channel.pipeline() != null) {
            try {
                channel.pipeline().fireUserEventTriggered(CompressionReorderEvent.INSTANCE);
            } catch (Exception ignored) {}
        }
    }

    public void onDisconnect() {
        this.currentConnection = null;
        this.serverHasViaVersion = false;
        this.detectedServerBrand = null;
    }

    /**
     * Called when server brand is received. Checks for ViaVersion presence.
     */
    public void onServerBrandReceived(String brand) {
        this.detectedServerBrand = brand;
        String lowerBrand = brand.toLowerCase();
        if (lowerBrand.contains("viaversion") ||
            lowerBrand.contains("viabackwards") ||
            lowerBrand.contains("viarewind") ||
            lowerBrand.contains("vialegacy")) {
            this.serverHasViaVersion = true;
            addon.logger().warn("ViaVersion detected on server! Brand: " + brand);
            addon.logger().warn("Double translation may cause issues. Consider setting ViaFlow to NATIVE.");
        }
    }

    public boolean serverHasViaVersion() {
        return serverHasViaVersion;
    }

    public String getDetectedServerBrand() {
        return detectedServerBrand;
    }

    public ProtocolVersion getTargetVersion() {
        ViaFlowConfiguration.ProtocolVersionSetting setting =
            addon.configuration().targetVersion().get();

        if (setting == ViaFlowConfiguration.ProtocolVersionSetting.NATIVE) {
            return nativeVersion;
        }

        ProtocolVersion version = setting.toProtocolVersion();
        return version != null ? version : nativeVersion;
    }

    public void setTargetVersion(ProtocolVersion version) {
        this.targetVersion = version;
    }

    public boolean isTranslationActive() {
        return currentConnection != null && !getTargetVersion().equals(nativeVersion);
    }

    public static ProtocolVersion getNativeVersion() {
        return nativeVersion;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public boolean needsTranslation(ProtocolVersion serverVersion) {
        return !serverVersion.equals(nativeVersion);
    }

    public ViaFlowAddon getAddon() {
        return addon;
    }

    public Logger getLogger() {
        return logger;
    }

    public File getConfigDir() {
        return configDir;
    }

    public UserConnection getCurrentConnection() {
        return currentConnection;
    }

    public static ViaFlowCommon getInstance() {
        return instance;
    }
}
