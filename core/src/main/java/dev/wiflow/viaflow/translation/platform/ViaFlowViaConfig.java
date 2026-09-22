package dev.wiflow.viaflow.translation.platform;

import com.viaversion.viaversion.configuration.AbstractViaConfig;
import java.io.File;
import java.util.logging.Logger;

/**
 * ViaVersion config with the options that would contact third parties turned off: the update
 * check, and telling servers which version the player really runs.
 */
final class ViaFlowViaConfig extends AbstractViaConfig {

    ViaFlowViaConfig(File configFile, Logger logger) {
        super(configFile, logger);
    }

    @Override
    public boolean isCheckForUpdates() {
        return false;
    }

    @Override
    public boolean sendPlayerDetails() {
        return false;
    }
}
