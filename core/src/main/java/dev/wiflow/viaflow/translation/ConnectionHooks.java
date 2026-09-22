package dev.wiflow.viaflow.translation;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.channel.Channel;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Entry points for the connection mixins of every game version.
 */
public final class ConnectionHooks {

    private static final AtomicReference<ProtocolVersion> PENDING_TARGET = new AtomicReference<>();
    private static volatile ProtocolVersion activeTarget;

    private ConnectionHooks() {
    }

    /**
     * Sets the server version for the connection that is about to open, or null to connect
     * natively.
     */
    public static void prepare(ProtocolVersion target) {
        PENDING_TARGET.set(target);
        activeTarget = null;
    }

    /**
     * Returns the server version of the current connection, or null if it isn't translated.
     */
    public static ProtocolVersion activeTarget() {
        return activeTarget;
    }

    public static void onDisconnect() {
        activeTarget = null;
    }

    public static void onChannelInit(Channel channel) {
        ProtocolVersion target = PENDING_TARGET.getAndSet(null);
        if (target == null) {
            return;
        }

        PipelineInjector.inject(channel, target);
        activeTarget = target;
    }

    public static void onCompression(Channel channel) {
        if (channel.attr(PipelineInjector.TARGET_VERSION).get() != null) {
            PipelineInjector.reorderForCompression(channel);
        }
    }
}
