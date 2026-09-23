package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.concurrent.CompletableFuture;
import net.labymod.api.client.network.server.ServerAddress;

/**
 * Finds the version Auto joins a server as by pinging it twice: as this client, which servers
 * answer with this client's version if they accept it and with their own otherwise, and without a
 * version, which they answer with the newest one they run.
 */
public final class ServerVersionDetector {

    private ServerVersionDetector() {
    }

    public static CompletableFuture<AutoChoice> detect(ServerAddress address) {
        ProtocolVersion nativeVersion = NativeVersion.get();
        CompletableFuture<Integer> asClient = StatusPing.protocol(address, nativeVersion.getVersion());
        CompletableFuture<Integer> withoutVersion = StatusPing.protocol(address, StatusPing.WITHOUT_VERSION);
        return asClient.thenCombine(withoutVersion,
            (answerAsClient, answerWithoutVersion) -> AutoChoice.of(nativeVersion, answerAsClient, answerWithoutVersion));
    }
}
