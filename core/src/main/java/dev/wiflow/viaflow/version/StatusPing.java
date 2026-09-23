package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.libs.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.labymod.api.client.network.server.ServerAddress;

/**
 * Sends the status request the server list uses, with any protocol. LabyMod's pinger always sends
 * the client's own.
 */
final class StatusPing {

    static final int WITHOUT_VERSION = -1;

    private static final int TIMEOUT_MILLIS = 2500;
    private static final int MAX_RESPONSE_LENGTH = 1 << 20;
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(task -> {
        Thread thread = new Thread(task, "ViaFlow ping");
        thread.setDaemon(true);
        return thread;
    });

    private StatusPing() {
    }

    /**
     * Completes with the protocol the server answered with, or -1 if it didn't answer.
     */
    static CompletableFuture<Integer> protocol(ServerAddress address, int protocol) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return ping(address, protocol);
            } catch (IOException | RuntimeException exception) {
                return -1;
            }
        }, EXECUTOR);
    }

    private static int ping(ServerAddress address, int protocol) throws IOException {
        ServerAddress resolved = address.resolve();
        try (Socket socket = new Socket()) {
            socket.connect(resolved.getAddress(), TIMEOUT_MILLIS);
            socket.setSoTimeout(TIMEOUT_MILLIS);

            ByteArrayOutputStream handshake = new ByteArrayOutputStream();
            DataOutputStream packet = new DataOutputStream(handshake);
            writeVarInt(packet, 0x00);
            writeVarInt(packet, protocol);
            byte[] host = address.getHost().getBytes(StandardCharsets.UTF_8);
            writeVarInt(packet, host.length);
            packet.write(host);
            packet.writeShort(resolved.getPort());
            writeVarInt(packet, 1); // Next state: status

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            writeVarInt(out, handshake.size());
            handshake.writeTo(out);
            out.write(new byte[] {1, 0x00}); // Status request
            out.flush();

            DataInputStream in = new DataInputStream(socket.getInputStream());
            readVarInt(in); // Packet length
            readVarInt(in); // Packet id
            int length = readVarInt(in);
            if (length < 0 || length > MAX_RESPONSE_LENGTH) {
                throw new IOException("Status response of " + length + " bytes");
            }

            byte[] json = new byte[length];
            in.readFully(json);
            return JsonParser.parseString(new String(json, StandardCharsets.UTF_8)).getAsJsonObject()
                .getAsJsonObject("version").get("protocol").getAsInt();
        }
    }

    private static void writeVarInt(DataOutputStream out, int value) throws IOException {
        while ((value & ~0x7F) != 0) {
            out.writeByte(value & 0x7F | 0x80);
            value >>>= 7;
        }
        out.writeByte(value);
    }

    private static int readVarInt(DataInputStream in) throws IOException {
        int value = 0;
        for (int shift = 0; shift < 35; shift += 7) {
            byte current = in.readByte();
            value |= (current & 0x7F) << shift;
            if ((current & 0x80) == 0) {
                return value;
            }
        }
        throw new IOException("VarInt longer than 5 bytes");
    }
}
