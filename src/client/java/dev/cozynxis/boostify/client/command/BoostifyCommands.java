package dev.cozynxis.boostify.client.command;

import dev.cozynxis.boostify.client.BoostifyClient;
import dev.cozynxis.boostify.client.compat.Compatibility;
import dev.cozynxis.boostify.client.config.ConfigManager;
import dev.cozynxis.boostify.client.perf.AdaptiveController;
import dev.cozynxis.boostify.client.perf.BoostifyStats;
import dev.cozynxis.boostify.client.screen.BoostifyScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class BoostifyCommands {
    private BoostifyCommands() {}

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
            ClientCommands.literal("boostify")
                .executes(ctx -> {
                    Minecraft client = Minecraft.getInstance();
                    client.execute(() -> client.gui.setScreen(new BoostifyScreen(client.gui.screen())));
                    return 1;
                })
                .then(ClientCommands.literal("reload").executes(ctx -> {
                    ConfigManager.load();
                    ctx.getSource().sendFeedback(Component.literal("§dBoostify §7config reloaded."));
                    return 1;
                }))
                .then(ClientCommands.literal("stats").executes(ctx -> {
                    Minecraft mc = Minecraft.getInstance();
                    int chunks = mc.level == null ? 0 : mc.level.getChunkSource().getLoadedChunksCount();
                    int entities = mc.level == null ? 0 : mc.level.getEntityCount();
                    ctx.getSource().sendFeedback(Component.literal(
                        "§dBoostify §8| §f" + mc.getFps() + " FPS §8| §f" + entities + " entities §8| §f" + chunks + " chunks"
                    ));
                    ctx.getSource().sendFeedback(Component.literal(
                        "§7Skipped: §f" + BoostifyStats.particlesSkipped() + " particles, "
                            + BoostifyStats.entitiesSkipped() + " entities, "
                            + BoostifyStats.blockEntitiesSkipped() + " block entities §8| §7pressure "
                            + AdaptiveController.pressure() + "/3"
                    ));
                    ctx.getSource().sendFeedback(Component.literal("§7Detected: §f" + Compatibility.summary()));
                    return 1;
                }))
                .then(ClientCommands.literal("performance").executes(ctx -> {
                    ConfigManager.get().applyPerformancePreset();
                    ConfigManager.save();
                    ctx.getSource().sendFeedback(Component.literal("§dBoostify §7Performance preset applied."));
                    return 1;
                }))
        ));
        BoostifyClient.LOGGER.info("Registered /boostify client commands");
    }
}
