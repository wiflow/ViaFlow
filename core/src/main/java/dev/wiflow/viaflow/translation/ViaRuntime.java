package dev.wiflow.viaflow.translation;

import com.viaversion.viabackwards.ViaBackwardsPlatformImpl;
import com.viaversion.viarewind.ViaRewindPlatformImpl;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.ProtocolManager;
import com.viaversion.viaversion.commands.ViaCommandHandler;
import com.viaversion.viaversion.platform.NoopInjector;
import dev.wiflow.viaflow.translation.platform.ViaFlowPlatform;
import dev.wiflow.viaflow.translation.platform.ViaFlowPlatformLoader;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import net.labymod.api.util.logging.Logging;

public final class ViaRuntime {

    private static final Object LOCK = new Object();
    private static volatile CompletableFuture<Void> startup;

    private ViaRuntime() {
    }

    /**
     * Starts ViaVersion on a background thread the first time it is needed, so sessions that stay
     * on the native version never load it. Later calls return the same future.
     */
    public static CompletableFuture<Void> start(File dataFolder, String version, Logging logging) {
        CompletableFuture<Void> current = startup;
        if (current != null) {
            return current;
        }

        synchronized (LOCK) {
            if (startup == null) {
                CompletableFuture<Void> future = new CompletableFuture<>();
                Thread thread = new Thread(() -> {
                    try {
                        load(dataFolder, version);
                        future.complete(null);
                    } catch (Throwable throwable) {
                        logging.error("ViaVersion failed to start", throwable);
                        future.completeExceptionally(throwable);
                    }
                }, "ViaFlow startup");
                thread.setDaemon(true);
                thread.start();
                startup = future;
            }
            return startup;
        }
    }

    private static void load(File dataFolder, String version) {
        dataFolder.mkdirs();
        ViaManagerImpl.initAndLoad(
            new ViaFlowPlatform(dataFolder, version),
            new NoopInjector(),
            new ViaCommandHandler(false),
            new ViaFlowPlatformLoader(),
            () -> {
                new ViaBackwardsPlatformImpl();
                new ViaRewindPlatformImpl();
            }
        );

        // The default limit of 50 steps is nearly used up between 1.8 and 26.x.
        ProtocolManager protocolManager = Via.getManager().getProtocolManager();
        protocolManager.setMaxProtocolPathSize(Integer.MAX_VALUE);
        protocolManager.setMaxPathDeltaIncrease(-1);
    }
}
