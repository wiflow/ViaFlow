package dev.wiflow.viaflow.commands;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.version.NativeVersion;
import dev.wiflow.viaflow.version.SelectableVersions;
import dev.wiflow.viaflow.version.TargetVersions;
import java.util.List;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class ViaFlowCommand extends Command {

    private final ViaFlowAddon addon;

    public ViaFlowCommand(ViaFlowAddon addon) {
        super("viaflow", "vf");
        this.addon = addon;
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
        if (arguments.length == 0) {
            this.showStatus();
            return true;
        }

        String argument = arguments[0];
        if (argument.equalsIgnoreCase("list")) {
            this.showVersions();
        } else if (argument.equalsIgnoreCase("help")) {
            this.displayMessage(Component.translatable("viaflow.command.usage", NamedTextColor.GRAY));
        } else if (argument.equalsIgnoreCase(TargetVersions.AUTO)) {
            this.addon.targetVersions().setAuto();
            this.showSelected();
        } else if (argument.equalsIgnoreCase(TargetVersions.NATIVE)) {
            this.select(null);
        } else {
            ProtocolVersion version = SelectableVersions.find(argument);
            if (version == null) {
                this.displayMessage(Component.translatable("viaflow.command.unknown",
                    NamedTextColor.RED, Component.text(argument)));
            } else {
                this.select(version);
            }
        }
        return true;
    }

    private void select(ProtocolVersion version) {
        this.addon.targetVersions().set(version);
        this.showSelected();
    }

    private void showSelected() {
        this.displayMessage(Component.translatable("viaflow.command.selected",
            NamedTextColor.GREEN, this.describeTarget()));
    }

    private void showStatus() {
        if (!this.addon.configuration().enabled().get()) {
            this.displayMessage(Component.translatable("viaflow.command.disabled", NamedTextColor.RED));
            return;
        }

        this.displayMessage(Component.translatable("viaflow.command.status", NamedTextColor.GRAY,
            Component.text(NativeVersion.get().getName(), NamedTextColor.WHITE), this.describeTarget()));
    }

    private void showVersions() {
        List<ProtocolVersion> versions = SelectableVersions.all();
        StringBuilder names = new StringBuilder();
        for (ProtocolVersion version : versions) {
            if (names.length() > 0) {
                names.append(", ");
            }
            names.append(version.getName());
        }
        this.displayMessage(Component.translatable("viaflow.command.list", NamedTextColor.GRAY,
            Component.text(names.toString(), NamedTextColor.WHITE)));
    }

    private Component describeTarget() {
        if (this.addon.targetVersions().isAuto()) {
            return Component.translatable("viaflow.settings.targetVersion.auto", NamedTextColor.WHITE);
        }

        ProtocolVersion target = this.addon.targetVersions().current();
        if (target == null) {
            return Component.translatable("viaflow.settings.targetVersion.native", NamedTextColor.WHITE,
                Component.text(NativeVersion.get().getName()));
        }
        return Component.text(target.getName(), NamedTextColor.WHITE);
    }
}
