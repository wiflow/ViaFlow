package dev.wiflow.viaflow.widgets;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.ViaFlowCommon;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;

public class ViaFlowHudWidget extends TextHudWidget<TextHudWidgetConfig> {

    private TextLine versionLine;
    private TextLine statusLine;

    public ViaFlowHudWidget() {
        super("viaflow_version");
    }

    @Override
    public void load(TextHudWidgetConfig config) {
        super.load(config);
        this.versionLine = createLine("Version", "---");
        this.statusLine = createLine("Status", "---");
    }

    @Override
    public void onTick(boolean isEditorContext) {
        if (ViaFlowAddon.getInstance() == null || ViaFlowCommon.getInstance() == null) {
            this.versionLine.updateAndFlush("Not initialized");
            this.statusLine.updateAndFlush("---");
            return;
        }

        if (!ViaFlowAddon.getInstance().configuration().enabled().get()) {
            this.versionLine.updateAndFlush("Disabled");
            this.statusLine.updateAndFlush("---");
            return;
        }

        ProtocolVersion targetVersion = ViaFlowCommon.getInstance().getTargetVersion();
        ProtocolVersion nativeVersion = ViaFlowCommon.getNativeVersion();

        this.versionLine.updateAndFlush(targetVersion.getName());

        if (ViaFlowCommon.getInstance().isTranslationActive()) {
            this.statusLine.updateAndFlush(nativeVersion.getName() + " -> " + targetVersion.getName());
        } else if (targetVersion.equals(nativeVersion)) {
            this.statusLine.updateAndFlush("Native");
        } else {
            this.statusLine.updateAndFlush("Ready");
        }
    }
}
