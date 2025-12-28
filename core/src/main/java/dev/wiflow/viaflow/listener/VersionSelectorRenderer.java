package dev.wiflow.viaflow.listener;

import dev.wiflow.viaflow.ViaFlowAddon;
import dev.wiflow.viaflow.ViaFlowConfiguration.ProtocolVersionSetting;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;
import net.labymod.api.event.client.input.MouseButtonEvent;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.event.client.render.GameRenderEvent;

/**
 * Listener for version cycling functionality.
 * Renders a version button on the multiplayer screen.
 */
public class VersionSelectorRenderer {

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_MARGIN = 5;

    private boolean isInScreen = false;
    private boolean isMultiplayerScreen = false;
    private String lastScreenInfo = "";
    private int screenWidth = 0;
    private int screenHeight = 0;

    @Subscribe
    public void onTick(GameTickEvent event) {
        if (event.phase() != Phase.POST) {
            return;
        }

        ViaFlowAddon addon = ViaFlowAddon.getInstance();
        if (addon == null || !addon.configuration().enabled().get()) {
            isInScreen = false;
            isMultiplayerScreen = false;
            return;
        }

        var screen = Laby.labyAPI().minecraft().minecraftWindow().currentScreen();
        isInScreen = (screen != null);

        if (!isInScreen) {
            isMultiplayerScreen = false;
            return;
        }

        // Update screen dimensions
        try {
            var window = Laby.labyAPI().minecraft().minecraftWindow();
            screenWidth = (int) window.getScaledWidth();
            screenHeight = (int) window.getScaledHeight();
        } catch (Exception e) {
            screenWidth = 800;
            screenHeight = 600;
        }

        // Try to get more info about the screen for detection
        String screenInfo = screen.getClass().getName();
        try {
            // Try to get toString which might have more info
            screenInfo += " | " + screen.toString();
        } catch (Exception e) {
            // ignore
        }

        // Log screen changes for debugging
        if (!screenInfo.equals(lastScreenInfo)) {
            lastScreenInfo = screenInfo;
            System.out.println("[ViaFlow] Screen: " + screenInfo);

            ProtocolVersionSetting currentVersion = addon.configuration().targetVersion().get();
            System.out.println("[ViaFlow] Version: " + currentVersion.getDisplayName() + " | Press F6 to cycle");
        }

        // For now, render on all screens until we can identify the multiplayer screen
        isMultiplayerScreen = true;
    }

    @Subscribe
    public void onRender(GameRenderEvent event) {
        if (!isInScreen || !isMultiplayerScreen) {
            return;
        }

        ViaFlowAddon addon = ViaFlowAddon.getInstance();
        if (addon == null || !addon.configuration().enabled().get()) {
            return;
        }

        Stack stack = event.stack();
        ProtocolVersionSetting currentVersion = addon.configuration().targetVersion().get();

        int x = screenWidth - BUTTON_WIDTH - BUTTON_MARGIN;
        int y = BUTTON_MARGIN;

        // Check if mouse is hovering
        MutableMouse mouse = Laby.labyAPI().minecraft().mouse();
        double mouseX = mouse.getX();
        double mouseY = mouse.getY();
        boolean hovering = mouseX >= x && mouseX <= x + BUTTON_WIDTH &&
                           mouseY >= y && mouseY <= y + BUTTON_HEIGHT;

        // Draw button background - brighter colors for visibility
        int bgColor = hovering ? 0xFF505050 : 0xFF2a2a2a;
        Laby.labyAPI().renderPipeline().rectangleRenderer()
            .pos(x, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT)
            .color(bgColor)
            .render(stack);

        // Draw border - bright green to make it visible
        int borderColor = hovering ? 0xFF00FF00 : 0xFF00AA00;
        // Top
        Laby.labyAPI().renderPipeline().rectangleRenderer()
            .pos(x, y, x + BUTTON_WIDTH, y + 1)
            .color(borderColor)
            .render(stack);
        // Bottom
        Laby.labyAPI().renderPipeline().rectangleRenderer()
            .pos(x, y + BUTTON_HEIGHT - 1, x + BUTTON_WIDTH, y + BUTTON_HEIGHT)
            .color(borderColor)
            .render(stack);
        // Left
        Laby.labyAPI().renderPipeline().rectangleRenderer()
            .pos(x, y, x + 1, y + BUTTON_HEIGHT)
            .color(borderColor)
            .render(stack);
        // Right
        Laby.labyAPI().renderPipeline().rectangleRenderer()
            .pos(x + BUTTON_WIDTH - 1, y, x + BUTTON_WIDTH, y + BUTTON_HEIGHT)
            .color(borderColor)
            .render(stack);
    }

    @Subscribe
    public void onMouseClick(MouseButtonEvent event) {
        if (!isInScreen || !isMultiplayerScreen) {
            return;
        }

        ViaFlowAddon addon = ViaFlowAddon.getInstance();
        if (addon == null || !addon.configuration().enabled().get()) {
            return;
        }

        MutableMouse mouse = Laby.labyAPI().minecraft().mouse();
        double mouseX = mouse.getX();
        double mouseY = mouse.getY();

        int x = screenWidth - BUTTON_WIDTH - BUTTON_MARGIN;
        int y = BUTTON_MARGIN;

        if (mouseX >= x && mouseX <= x + BUTTON_WIDTH &&
            mouseY >= y && mouseY <= y + BUTTON_HEIGHT) {

            ProtocolVersionSetting current = addon.configuration().targetVersion().get();
            ProtocolVersionSetting next = current.next();
            addon.configuration().targetVersion().set(next);

            System.out.println("[ViaFlow] Version changed to: " + next.getDisplayName());
        }
    }

    @Subscribe
    public void onKeyPress(KeyEvent event) {
        if (!isInScreen) {
            return;
        }

        if (event.state() != State.PRESS) {
            return;
        }

        if (event.key() != Key.F6) {
            return;
        }

        ViaFlowAddon addon = ViaFlowAddon.getInstance();
        if (addon == null || !addon.configuration().enabled().get()) {
            return;
        }

        ProtocolVersionSetting current = addon.configuration().targetVersion().get();
        ProtocolVersionSetting next = current.next();
        addon.configuration().targetVersion().set(next);

        System.out.println("[ViaFlow] Version changed to: " + next.getDisplayName());
        event.setCancelled(true);
    }
}
