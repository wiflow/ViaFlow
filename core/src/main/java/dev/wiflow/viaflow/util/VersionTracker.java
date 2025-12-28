package dev.wiflow.viaflow.util;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VersionTracker {

    private static final Map<InetAddress, ProtocolVersion> SERVER_VERSIONS = new ConcurrentHashMap<>();
    private static ProtocolVersion lastPingedVersion = null;

    public static void storeServerVersion(InetSocketAddress address, ProtocolVersion version) {
        if (address != null && address.getAddress() != null && version != null) {
            SERVER_VERSIONS.put(address.getAddress(), version);
            lastPingedVersion = version;
        }
    }

    public static ProtocolVersion getServerVersion(InetSocketAddress address) {
        if (address != null && address.getAddress() != null) {
            return SERVER_VERSIONS.get(address.getAddress());
        }
        return null;
    }

    public static ProtocolVersion getLastPingedVersion() {
        return lastPingedVersion;
    }

    public static void clear() {
        SERVER_VERSIONS.clear();
        lastPingedVersion = null;
    }

    public static void remove(InetSocketAddress address) {
        if (address != null && address.getAddress() != null) {
            SERVER_VERSIONS.remove(address.getAddress());
        }
    }
}
