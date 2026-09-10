package dev.cozynxis.boostify.client;

import dev.cozynxis.boostify.client.command.BoostifyCommands;
import dev.cozynxis.boostify.client.compat.Compatibility;
import dev.cozynxis.boostify.client.config.ConfigManager;
import dev.cozynxis.boostify.client.hud.BoostifyHud;
import dev.cozynxis.boostify.client.perf.AdaptiveController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BoostifyClient implements ClientModInitializer {
    public static final String MOD_ID = "boostify";
    public static final String MOD_NAME = "Boostify";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitializeClient() {
        ConfigManager.load();
        Compatibility.detect();
        BoostifyCommands.register();
        BoostifyHud.register();

        ClientTickEvents.END_CLIENT_TICK.register(AdaptiveController::tick);
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            AdaptiveController.restoreTemporaryVanillaOptions(client);
            ConfigManager.save();
        });

        LOGGER.info("Boostify {} initialized. Compatibility: {}", VERSION, Compatibility.summary());
    }
}
