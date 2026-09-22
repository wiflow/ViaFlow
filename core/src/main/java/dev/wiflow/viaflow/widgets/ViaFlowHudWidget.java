package dev.wiflow.viaflow.widgets;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import dev.wiflow.viaflow.translation.ConnectionHooks;
import dev.wiflow.viaflow.version.NativeVersion;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;

/**
 * Shows the server version the current connection is translated to. Hidden while playing natively.
 */
public class ViaFlowHudWidget extends TextHudWidget<TextHudWidgetConfig> {

    private TextLine line;
    private ProtocolVersion shownTarget;

    public ViaFlowHudWidget() {
        super("viaflow_version");
    }

    @Override
    public void load(TextHudWidgetConfig config) {
        super.load(config);
        this.shownTarget = ConnectionHooks.activeTarget();
        this.line = this.createLine(Component.translatable("viaflow.hudWidget.viaflow_version.server"),
            describe(this.shownTarget));
    }

    @Override
    public void onTick(boolean isEditorContext) {
        ProtocolVersion target = ConnectionHooks.activeTarget();
        if (target != this.shownTarget) {
            this.shownTarget = target;
            this.line.updateAndFlush(describe(target));
        }
    }

    @Override
    public boolean isVisibleInGame() {
        return ConnectionHooks.activeTarget() != null;
    }

    private static String describe(ProtocolVersion target) {
        return (target == null ? NativeVersion.get() : target).getName();
    }
}
