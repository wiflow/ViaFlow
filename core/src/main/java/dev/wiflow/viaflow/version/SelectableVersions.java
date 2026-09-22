package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.protocol.version.VersionType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The server versions that can be picked: every release from 1.8 on.
 */
public final class SelectableVersions {

    private SelectableVersions() {
    }

    /**
     * Returns the selectable versions, newest first.
     */
    public static List<ProtocolVersion> all() {
        List<ProtocolVersion> versions = new ArrayList<>();
        for (ProtocolVersion version : ProtocolVersion.getProtocols()) {
            if (isSelectable(version)) {
                versions.add(version);
            }
        }
        versions.sort(Collections.reverseOrder());
        return versions;
    }

    /**
     * Looks up a version by its ViaVersion name or by a game version it covers, like "1.8.9".
     * Returns null if there is no such selectable version.
     */
    public static ProtocolVersion find(String name) {
        ProtocolVersion version = ProtocolVersion.getClosest(name);
        return version != null && version.isKnown() && isSelectable(version) ? version : null;
    }

    private static boolean isSelectable(ProtocolVersion version) {
        return version.getVersionType() == VersionType.RELEASE
            && version.newerThanOrEqualTo(ProtocolVersion.v1_8);
    }
}
