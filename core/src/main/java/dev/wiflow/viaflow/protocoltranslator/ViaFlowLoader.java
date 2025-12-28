package dev.wiflow.viaflow.protocoltranslator;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.providers.ViaProviders;
import com.viaversion.viaversion.api.protocol.version.VersionProvider;
import com.viaversion.viaversion.protocols.v1_8to1_9.provider.MovementTransmitterProvider;
import com.viaversion.vialoader.impl.viaversion.VLLoader;
import dev.wiflow.viaflow.protocoltranslator.provider.ViaFlowMovementTransmitterProvider;
import dev.wiflow.viaflow.protocoltranslator.provider.ViaFlowVersionProvider;

public class ViaFlowLoader extends VLLoader {

    @Override
    public void load() {
        super.load();
        ViaProviders providers = Via.getManager().getProviders();
        providers.use(VersionProvider.class, new ViaFlowVersionProvider());
        providers.use(MovementTransmitterProvider.class, new ViaFlowMovementTransmitterProvider());
    }
}
