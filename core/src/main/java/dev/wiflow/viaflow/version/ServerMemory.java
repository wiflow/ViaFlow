package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.labymod.api.client.network.server.ServerAddress;
import net.labymod.api.util.logging.Logging;

/**
 * The version each server was last joined as, kept across game starts so Auto joins known servers
 * without pinging them first.
 */
public final class ServerMemory {

    private static final String NATIVE = "native";

    private final Path file;
    private final Logging logging;
    private final Map<String, String> versions = new ConcurrentHashMap<>();

    public ServerMemory(Path file, Logging logging) {
        this.file = file;
        this.logging = logging;
        this.load();
    }

    /**
     * Returns the version a server was last joined as, or null if it wasn't joined yet.
     */
    public AutoChoice get(ServerAddress server) {
        String name = this.versions.get(key(server));
        if (name == null) {
            return null;
        }
        if (NATIVE.equals(name)) {
            return AutoChoice.fixed(null);
        }

        ProtocolVersion version = SelectableVersions.find(name);
        return version != null ? AutoChoice.fixed(version) : null;
    }

    /**
     * Remembers the version a server was joined as, null for natively.
     */
    public void remember(ServerAddress server, ProtocolVersion version) {
        String name = version != null ? version.getName() : NATIVE;
        if (!name.equals(this.versions.put(key(server), name))) {
            this.save();
        }
    }

    public void forget(ServerAddress server) {
        if (this.versions.remove(key(server)) != null) {
            this.save();
        }
    }

    private static String key(ServerAddress server) {
        return server.getHost().toLowerCase(Locale.ROOT) + ":" + server.getPort();
    }

    private void load() {
        if (!Files.isRegularFile(this.file)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(this.file, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, JsonElement> entry : JsonParser.parseReader(reader).getAsJsonObject().entrySet()) {
                this.versions.put(entry.getKey(), entry.getValue().getAsString());
            }
        } catch (IOException | RuntimeException exception) {
            this.logging.warn("Could not read the remembered server versions", exception);
        }
    }

    private synchronized void save() {
        JsonObject json = new JsonObject();
        this.versions.forEach(json::addProperty);
        try {
            Files.createDirectories(this.file.getParent());
            Files.writeString(this.file, json.toString(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            this.logging.warn("Could not save the remembered server versions", exception);
        }
    }
}
