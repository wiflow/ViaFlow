package dev.wiflow.viaflow.commands;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.ViaFlowCommon;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import java.util.Set;

public class ViaFlowCommand extends Command {

    private final ViaFlowAddon addon;

    public ViaFlowCommand(ViaFlowAddon addon) {
        super("viaflow", "vf");
        this.addon = addon;
    }

    @Override
    public boolean execute(String prefix, String[] args) {
        if (args.length == 0) {
            showInfo();
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "list" -> showVersionList();
            case "info" -> showInfo();
            case "help" -> showHelp();
            default -> {
                ProtocolVersion version = parseVersion(args[0]);
                if (version != null) {
                    setVersion(version);
                } else {
                    displayMessage(Component.text("Unknown version: " + args[0], NamedTextColor.RED));
                    showHelp();
                }
            }
        }

        return true;
    }

    private void showInfo() {
        ProtocolVersion nativeVersion = ViaFlowCommon.getNativeVersion();
        ProtocolVersion targetVersion = ViaFlowCommon.getInstance().getTargetVersion();

        displayMessage(Component.text("=== ViaFlow ===", NamedTextColor.GOLD));
        displayMessage(Component.text("Client: ", NamedTextColor.GRAY)
            .append(Component.text(nativeVersion.getName(), NamedTextColor.GREEN)));
        displayMessage(Component.text("Target: ", NamedTextColor.GRAY)
            .append(Component.text(targetVersion.getName(), NamedTextColor.AQUA)));
        displayMessage(Component.text("Enabled: ", NamedTextColor.GRAY)
            .append(Component.text(addon.configuration().enabled().get() ? "Yes" : "No",
                addon.configuration().enabled().get() ? NamedTextColor.GREEN : NamedTextColor.RED)));
    }

    private void showVersionList() {
        Set<ProtocolVersion> versions = Via.getManager().getProtocolManager().getSupportedVersions();

        displayMessage(Component.text("=== Supported Versions ===", NamedTextColor.GOLD));

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (ProtocolVersion version : versions) {
            if (count > 0) sb.append(", ");
            sb.append(version.getName());
            count++;
            if (count % 8 == 0) {
                displayMessage(Component.text(sb.toString(), NamedTextColor.GRAY));
                sb = new StringBuilder();
            }
        }

        if (sb.length() > 0) {
            displayMessage(Component.text(sb.toString(), NamedTextColor.GRAY));
        }

        displayMessage(Component.text("Total: " + versions.size() + " versions", NamedTextColor.GREEN));
    }

    private void showHelp() {
        displayMessage(Component.text("=== ViaFlow Commands ===", NamedTextColor.GOLD));
        displayMessage(Component.text("/viaflow", NamedTextColor.AQUA)
            .append(Component.text(" - Show info", NamedTextColor.GRAY)));
        displayMessage(Component.text("/viaflow list", NamedTextColor.AQUA)
            .append(Component.text(" - List versions", NamedTextColor.GRAY)));
        displayMessage(Component.text("/viaflow <version>", NamedTextColor.AQUA)
            .append(Component.text(" - Set target version", NamedTextColor.GRAY)));
    }

    private void setVersion(ProtocolVersion version) {
        ViaFlowCommon.getInstance().setTargetVersion(version);
        displayMessage(Component.text("Target version: ", NamedTextColor.GREEN)
            .append(Component.text(version.getName(), NamedTextColor.AQUA)));
        displayMessage(Component.text("Reconnect to apply.", NamedTextColor.YELLOW));
    }

    private ProtocolVersion parseVersion(String input) {
        for (ProtocolVersion version : Via.getManager().getProtocolManager().getSupportedVersions()) {
            if (version.getName().equalsIgnoreCase(input)) {
                return version;
            }
        }

        if (!input.startsWith("1.")) {
            return parseVersion("1." + input);
        }

        for (ProtocolVersion version : Via.getManager().getProtocolManager().getSupportedVersions()) {
            if (version.getName().toLowerCase().contains(input.toLowerCase())) {
                return version;
            }
        }

        return null;
    }
}
