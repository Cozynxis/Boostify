package dev.cozynxis.boostify.client.screen;

import dev.cozynxis.boostify.client.compat.Compatibility;
import dev.cozynxis.boostify.client.config.BoostifyConfig;
import dev.cozynxis.boostify.client.config.ConfigManager;
import dev.cozynxis.boostify.client.perf.AdaptiveController;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class BoostifyScreen extends Screen {
    private final Screen parent;

    public BoostifyScreen(Screen parent) {
        super(Component.literal("Boostify Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        BoostifyConfig c = ConfigManager.get();
        int center = this.width / 2;
        int left = center - 206;
        int right = center + 6;
        int y = 66;
        int w = 200;
        int h = 20;
        int gap = 24;

        addRenderableWidget(toggle(left, y, w, h, "Boostify", () -> c.enabled, v -> c.enabled = v));
        addRenderableWidget(toggle(right, y, w, h, "Adaptive optimizer", () -> c.adaptiveOptimizer, v -> c.adaptiveOptimizer = v));
        y += gap;

        addRenderableWidget(cycle(left, y, w, h, () -> "Target FPS: " + c.targetFps, () -> {
            int[] values = {60, 90, 120, 144};
            c.targetFps = next(values, c.targetFps);
        }));
        addRenderableWidget(toggle(right, y, w, h, "Dynamic render distance", () -> c.dynamicRenderDistance, v -> c.dynamicRenderDistance = v));
        y += gap;

        addRenderableWidget(toggle(left, y, w, h, "Particle optimizer", () -> c.particleOptimizer, v -> c.particleOptimizer = v));
        addRenderableWidget(cycle(right, y, w, h, () -> "Max chunk reduction: " + c.maxRenderDistanceReduction, () -> {
            c.maxRenderDistanceReduction = (c.maxRenderDistanceReduction + 1) % 4;
        }));
        y += gap;

        addRenderableWidget(cycle(left, y, w, h, () -> "Particle amount: " + c.particlePercent + "%", () -> {
            int[] values = {100, 80, 65, 50};
            c.particlePercent = next(values, c.particlePercent);
        }));
        addRenderableWidget(toggle(right, y, w, h, "Entity distance culling", () -> c.entityDistanceCulling, v -> c.entityDistanceCulling = v));
        y += gap;

        addRenderableWidget(toggle(left, y, w, h, "Block-entity culling", () -> c.blockEntityDistanceCulling, v -> c.blockEntityDistanceCulling = v));
        addRenderableWidget(cycle(right, y, w, h, () -> "Entity range: " + c.entityDistance + " blocks", () -> {
            int[] values = {96, 112, 128, 160};
            c.entityDistance = next(values, c.entityDistance);
        }));
        y += gap;

        addRenderableWidget(cycle(left, y, w, h, () -> "Block-entity range: " + c.blockEntityDistance, () -> {
            int[] values = {56, 72, 80, 96};
            c.blockEntityDistance = next(values, c.blockEntityDistance);
        }));
        addRenderableWidget(toggle(right, y, w, h, "FPS/RAM overlay", () -> c.overlayEnabled, v -> c.overlayEnabled = v));
        y += gap;

        addRenderableWidget(toggle(left, y, w, h, "Idle FPS limiter", () -> c.idleFpsLimiter, v -> c.idleFpsLimiter = v));
        addRenderableWidget(cycle(right, y, w, h, () -> "Idle FPS: " + c.idleFps, () -> c.idleFps = c.idleFps == 30 ? 15 : 30));
        y += gap;

        addRenderableWidget(toggle(left, y, w, h, "Compatibility mode", () -> c.compatibilityMode, v -> c.compatibilityMode = v));
        addRenderableWidget(Button.builder(Component.literal("§dPerformance preset"), b -> {
            c.applyPerformancePreset();
            ConfigManager.save();
            rebuildWidgets();
        }).bounds(right, y, w, h).build());

        int bottom = Math.min(this.height - 30, y + 36);
        addRenderableWidget(Button.builder(Component.literal("Reset balanced"), b -> {
            c.resetBalancedDefaults();
            ConfigManager.save();
            rebuildWidgets();
        }).bounds(center - 206, bottom, 130, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Save & Done"), b -> onClose())
            .bounds(center + 76, bottom, 130, 20).build());
    }

    private Button toggle(int x, int y, int w, int h, String name, BoolGetter getter, BoolSetter setter) {
        return Button.builder(toggleLabel(name, getter.get()), button -> {
            setter.set(!getter.get());
            ConfigManager.save();
            button.setMessage(toggleLabel(name, getter.get()));
        }).bounds(x, y, w, h).build();
    }

    private Button cycle(int x, int y, int w, int h, TextGetter text, Runnable next) {
        return Button.builder(Component.literal(text.get()), button -> {
            next.run();
            ConfigManager.save();
            button.setMessage(Component.literal(text.get()));
        }).bounds(x, y, w, h).build();
    }

    private static Component toggleLabel(String name, boolean enabled) {
        return Component.literal(name + ": " + (enabled ? "§aON" : "§cOFF"));
    }

    private static int next(int[] values, int current) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == current) return values[(i + 1) % values.length];
        }
        return values[0];
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        if (!ConfigManager.get().enabled) {
            AdaptiveController.restoreTemporaryVanillaOptions(this.minecraft);
        }
        this.minecraft.gui.setScreen(this.parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);
        int center = this.width / 2;
        graphics.text(this.font, "§d§lBOOSTIFY", center - 35, 18, 0xFFFFFFFF, true);
        graphics.text(this.font, "Safe/balanced FPS + frametime optimizer for Minecraft 26.2", center - 167, 34, 0xFFBBBBBB, false);
        graphics.text(this.font, "Detected: " + Compatibility.summary(), center - 167, 47, 0xFF8C8C9A, false);
        graphics.text(this.font, "Adaptive load: " + AdaptiveController.pressure() + "/3", center + 75, 47, 0xFF8C8C9A, false);
    }

    @FunctionalInterface private interface BoolGetter { boolean get(); }
    @FunctionalInterface private interface BoolSetter { void set(boolean value); }
    @FunctionalInterface private interface TextGetter { String get(); }
}
