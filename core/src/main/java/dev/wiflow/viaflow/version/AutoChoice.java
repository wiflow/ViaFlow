package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

/**
 * The version Auto joins a server as, null for natively, and the version to join again as if the
 * server turns the first one away before the world loads.
 */
public record AutoChoice(ProtocolVersion version, ProtocolVersion fallback) {

    public static AutoChoice fixed(ProtocolVersion version) {
        return new AutoChoice(version, null);
    }

    /**
     * Picks from what a server answered when pinged as this client and when pinged without a
     * version, which servers answer with the newest version they run. Proxies answer the first
     * with this client's version whenever they support it, even if the servers behind them don't,
     * so those are joined natively first with their newest version as the fallback.
     */
    public static AutoChoice of(ProtocolVersion nativeVersion, int answerAsClient, int answerWithoutVersion) {
        ProtocolVersion newest = SelectableVersions.forServer(nativeVersion, answerWithoutVersion);
        if (answerAsClient == nativeVersion.getVersion()) {
            return new AutoChoice(null, newest);
        }

        ProtocolVersion version = SelectableVersions.forServer(nativeVersion, answerAsClient);
        return fixed(version != null ? version : newest);
    }
}
