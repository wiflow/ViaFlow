package dev.wiflow.viaflow.version;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ServerDetailsTest {

    @Test
    void readsTheVersionAfterALengthPrefix() {
        String json = "{\"specVersion\":1,\"version\":772,\"versionName\":\"1.21.7-1.21.8\",\"versionType\":\"RELEASE\"}";
        byte[] text = json.getBytes(StandardCharsets.UTF_8);
        byte[] payload = new byte[text.length + 1];
        payload[0] = (byte) text.length;
        System.arraycopy(text, 0, payload, 1, text.length);

        assertEquals(772, ServerDetails.protocol(payload));
    }

    @Test
    void returnsMinusOneWithoutAVersion() {
        assertEquals(-1, ServerDetails.protocol("{\"specVersion\":1}".getBytes(StandardCharsets.UTF_8)));
        assertEquals(-1, ServerDetails.protocol("not json".getBytes(StandardCharsets.UTF_8)));
        assertEquals(-1, ServerDetails.protocol(new byte[0]));
    }
}
