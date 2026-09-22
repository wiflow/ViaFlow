package dev.wiflow.viaflow.version;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownEntries;

public final class VersionEntries implements DropdownEntries<String> {

    @Override
    public List<String> entries() {
        ProtocolVersion nativeVersion = NativeVersion.get();
        List<ProtocolVersion> versions = SelectableVersions.all();
        List<String> entries = new ArrayList<>(versions.size() + 1);
        entries.add(TargetVersions.NATIVE);
        for (ProtocolVersion version : versions) {
            if (!version.equals(nativeVersion)) {
                entries.add(version.getName());
            }
        }
        return entries;
    }

    @Override
    public Component displayName(String entry) {
        if (TargetVersions.NATIVE.equals(entry)) {
            return Component.translatable("viaflow.settings.targetVersion.native",
                Component.text(NativeVersion.get().getName()));
        }
        return Component.text(entry);
    }
}
