package dev.wiflow.viaflow.listener;

import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.version.VersionEntries;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.renderer.DefaultEntryRenderer;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ActivityInitializeEvent;

/**
 * Adds a server version picker below the buttons of LabyMod's server list.
 */
public class ServerListListener {

    private static final String SERVER_LIST = "labymod:private_server_list";

    private final ViaFlowAddon addon;

    public ServerListListener(ViaFlowAddon addon) {
        this.addon = addon;
    }

    @Subscribe
    public void onActivityInitialize(ActivityInitializeEvent event) {
        if (!SERVER_LIST.equals(event.getIdentifier()) || !this.addon.configuration().enabled().get()) {
            return;
        }

        Activity activity = event.activity();
        if (!(activity.document().getChildRecursive("button-container") instanceof FlexibleContentWidget buttons)) {
            return;
        }

        activity.addStyle("viaflow", "server-list.lss");
        buttons.addContentInitialized(this.createVersionRow());
    }

    private FlexibleContentWidget createVersionRow() {
        ConfigProperty<String> setting = this.addon.configuration().targetVersion();
        VersionEntries entries = new VersionEntries();

        DropdownWidget<String> dropdown = new DropdownWidget<>();
        dropdown.addId("viaflow-version-dropdown");
        dropdown.setEntrySupplier(entries::entries);
        ((DefaultEntryRenderer<String>) dropdown.entryRenderer()).setDisplayNameProvider(entries::displayName);
        dropdown.setSelected(setting.get());
        dropdown.setChangeListener(setting::set);

        ComponentWidget label = ComponentWidget.i18n("viaflow.multiplayer.version");
        label.addId("viaflow-version-label");

        FlexibleContentWidget row = new FlexibleContentWidget();
        row.addId("viaflow-version-row");
        row.addContent(label);
        row.addFlexibleContent(dropdown);
        return row;
    }
}
