package dev.wiflow.viaflow.version;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import org.junit.jupiter.api.Test;

class AutoChoiceTest {

    private static final ProtocolVersion CLIENT = ProtocolVersion.v1_8;

    @Test
    void joinsServersOnTheClientVersionNatively() {
        assertEquals(new AutoChoice(null, null), AutoChoice.of(CLIENT, CLIENT.getVersion(), CLIENT.getVersion()));
    }

    @Test
    void joinsServersOnAnotherVersionAsThatVersion() {
        int server = ProtocolVersion.v1_21_7.getVersion();
        assertEquals(AutoChoice.fixed(ProtocolVersion.v1_21_7), AutoChoice.of(CLIENT, server, server));
    }

    @Test
    void joinsProxiesThatListTheClientVersionNativelyFirst() {
        int newest = ProtocolVersion.v26_2.getVersion();
        assertEquals(new AutoChoice(null, ProtocolVersion.v26_2), AutoChoice.of(CLIENT, CLIENT.getVersion(), newest));
    }

    @Test
    void joinsNativelyWhenTheServerDoesNotAnswer() {
        assertEquals(new AutoChoice(null, null), AutoChoice.of(CLIENT, -1, -1));
    }

    @Test
    void usesTheNewestVersionWhenTheFirstAnswerCannotBePicked() {
        int newest = ProtocolVersion.v1_21_7.getVersion();
        assertEquals(AutoChoice.fixed(ProtocolVersion.v1_21_7), AutoChoice.of(CLIENT, 123456, newest));
    }
}
