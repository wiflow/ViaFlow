package dev.wiflow.viaflow.protocoltranslator;

import com.viaversion.vialoader.impl.viaversion.VLInjector;
import com.viaversion.vialoader.netty.VLLegacyPipeline;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectLinkedOpenHashSet;
import dev.wiflow.viaflow.ViaFlowCommon;
import java.util.SortedSet;

public class ViaFlowInjector extends VLInjector {

    @Override
    public String getDecoderName() {
        return VLLegacyPipeline.VIA_DECODER_NAME;
    }

    @Override
    public String getEncoderName() {
        return VLLegacyPipeline.VIA_ENCODER_NAME;
    }

    @Override
    public ProtocolVersion getServerProtocolVersion() {
        ProtocolVersion nativeVersion = ViaFlowCommon.getNativeVersion();
        return nativeVersion != null ? nativeVersion : super.getServerProtocolVersion();
    }

    @Override
    public SortedSet<ProtocolVersion> getServerProtocolVersions() {
        final SortedSet<ProtocolVersion> versions = new ObjectLinkedOpenHashSet<>();
        versions.addAll(ProtocolVersion.getProtocols());
        return versions;
    }
}
