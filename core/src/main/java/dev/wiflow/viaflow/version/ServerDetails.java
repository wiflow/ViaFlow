package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonParser;
import java.nio.charset.StandardCharsets;

/**
 * The details ViaVersion on a server sends to clients. They name that server's own version, even
 * behind a proxy that answers pings for a different one.
 */
public final class ServerDetails {

    public static final String CHANNEL = "vv:server_details";

    private ServerDetails() {
    }

    /**
     * Returns the server's protocol from the details payload, or -1 if it has none.
     */
    public static int protocol(byte[] payload) {
        String text = new String(payload, StandardCharsets.UTF_8);
        int start = text.indexOf('{');
        if (start == -1) {
            return -1;
        }

        try {
            JsonElement version = JsonParser.parseString(text.substring(start)).getAsJsonObject().get("version");
            return version != null ? version.getAsInt() : -1;
        } catch (RuntimeException exception) {
            return -1;
        }
    }
}
