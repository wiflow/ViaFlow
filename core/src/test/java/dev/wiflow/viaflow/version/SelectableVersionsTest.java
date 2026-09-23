package dev.wiflow.viaflow.version;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.List;
import org.junit.jupiter.api.Test;

class SelectableVersionsTest {

    @Test
    void everyBuiltMinecraftVersionIsKnownToViaVersion() {
        String versions = System.getProperty("viaflow.minecraftVersions");
        assertNotNull(versions, "viaflow.minecraftVersions is not set");
        for (String version : versions.split(";")) {
            assertNotNull(SelectableVersions.find(version), "ViaVersion does not know Minecraft " + version);
        }
    }

    @Test
    void versionsAreNewestFirstDownTo1_8() {
        List<ProtocolVersion> versions = SelectableVersions.all();
        assertEquals(ProtocolVersion.v1_8, versions.get(versions.size() - 1));
        for (int i = 1; i < versions.size(); i++) {
            assertTrue(versions.get(i - 1).newerThan(versions.get(i)));
        }
    }

    @Test
    void findsVersionsByGameVersion() {
        assertEquals(ProtocolVersion.v1_8, SelectableVersions.find("1.8.9"));
        assertEquals(ProtocolVersion.v1_21_7, SelectableVersions.find("1.21.8"));
        assertEquals(ProtocolVersion.v1_21_7, SelectableVersions.find(ProtocolVersion.v1_21_7.getName()));
    }

    @Test
    void rejectsUnknownAndOlderVersions() {
        assertNull(SelectableVersions.find("native"));
        assertNull(SelectableVersions.find("1.7.10"));
        assertNull(SelectableVersions.find("banana"));
    }

    @Test
    void connectsNativelyToServersThatAcceptTheClientVersion() {
        assertNull(SelectableVersions.forServer(ProtocolVersion.v1_8, ProtocolVersion.v1_8.getVersion()));
        assertNull(SelectableVersions.forServer(ProtocolVersion.v26_2, ProtocolVersion.v26_2.getVersion()));
    }

    @Test
    void connectsAsTheVersionTheServerReports() {
        assertEquals(ProtocolVersion.v26_2,
            SelectableVersions.forServer(ProtocolVersion.v1_8, ProtocolVersion.v26_2.getVersion()));
        assertEquals(ProtocolVersion.v1_8,
            SelectableVersions.forServer(ProtocolVersion.v26_2, ProtocolVersion.v1_8.getVersion()));
    }

    @Test
    void connectsNativelyWhenTheReportedVersionCannotBePicked() {
        assertNull(SelectableVersions.forServer(ProtocolVersion.v1_8, -1));
        assertNull(SelectableVersions.forServer(ProtocolVersion.v1_8, 123456));
        assertNull(SelectableVersions.forServer(ProtocolVersion.v26_2, ProtocolVersion.v1_7_6.getVersion()));
    }
}
