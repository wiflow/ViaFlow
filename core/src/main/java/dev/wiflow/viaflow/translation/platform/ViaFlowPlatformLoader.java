package dev.wiflow.viaflow.translation.platform;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;

public final class ViaFlowPlatformLoader implements ViaPlatformLoader {

    @Override
    public void load() {
        Via.getManager().getProviders().use(VersionProvider.class, new ChannelVersionProvider());
    }

    @Override
    public void unload() {
    }
}
