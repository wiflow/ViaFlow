package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.labymod.api.Laby;

public final class NativeVersion {

    private static volatile ProtocolVersion version;

    private NativeVersion() {
    }

    /**
     * Returns the protocol this client speaks. It is not {@link ProtocolVersion#isKnown() known}
     * when the bundled ViaVersion is older than the client.
     */
    public static ProtocolVersion get() {
        ProtocolVersion current = version;
        if (current == null) {
            current = ProtocolVersion.getProtocol(Laby.labyAPI().minecraft().getProtocolVersion());
            version = current;
        }
        return current;
    }
}
