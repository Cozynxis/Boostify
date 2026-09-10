package dev.cozynxis.boostify.client.hud;

import dev.cozynxis.boostify.client.BoostifyClient;
import dev.cozynxis.boostify.client.config.ConfigManager;
import dev.cozynxis.boostify.client.perf.AdaptiveController;
import dev.cozynxis.boostify.client.perf.BoostifyStats;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public final class BoostifyHud {
    private BoostifyHud() {}

    public static void register() {
        HudElementRegistry.addLast(
            Identifier.fromNamespaceAndPath(BoostifyClient.MOD_ID, "performance_overlay"),
            BoostifyHud::extract
        );
    }

    private static void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (!ConfigManager.get().enabled || !ConfigManager.get().overlayEnabled) return;

        Minecraft mc = Minecraft.getInstance();
        long used = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L / 1024L;
        long max = Runtime.getRuntime().maxMemory() / 1024L / 1024L;
        int fps = Math.max(0, mc.getFps());
        double frameMs = fps <= 0 ? 0.0 : 1000.0 / fps;
        int entities = mc.level == null ? 0 : mc.level.getEntityCount();
        int chunks = mc.level == null ? 0 : mc.level.getChunkSource().getLoadedChunksCount();

        String[] lines = {
            "§d§lBoostify §f" + fps + " FPS",
            String.format("§7Frame §f%.1f ms §8| §7RAM §f%d/%d MB", frameMs, used, max),
            "§7Entities §f" + entities + " §8| §7Chunks §f" + chunks,
            "§7Load §f" + AdaptiveController.pressure() + "/3 §8| §7Culled §f"
                + (BoostifyStats.entitiesSkipped() + BoostifyStats.blockEntitiesSkipped()),
            "§7Particles saved §f" + BoostifyStats.particlesSkipped()
        };

        int x = 7;
        int y = 7;
        int width = 190;
        int height = 8 + lines.length * 11;
        graphics.fill(x - 4, y - 4, x + width, y + height, 0xA0101018);
        graphics.fill(x - 4, y - 4, x - 1, y + height, 0xFF9B59FF);

        for (int i = 0; i < lines.length; i++) {
            graphics.text(mc.font, lines[i], x, y + i * 11, 0xFFFFFFFF, true);
        }
    }
}
