package dev.cozynxis.boostify.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.cozynxis.boostify.client.BoostifyClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("boostify.json");
    private static BoostifyConfig config = new BoostifyConfig();

    private ConfigManager() {}

    public static BoostifyConfig get() {
        return config;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            BoostifyConfig loaded = GSON.fromJson(reader, BoostifyConfig.class);
            if (loaded != null) config = loaded;
            sanitize();
        } catch (Exception e) {
            BoostifyClient.LOGGER.warn("Could not read Boostify config; using defaults.", e);
            config = new BoostifyConfig();
        }
    }

    public static void save() {
        sanitize();
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            BoostifyClient.LOGGER.warn("Could not save Boostify config.", e);
        }
    }

    private static void sanitize() {
        config.targetFps = clamp(config.targetFps, 30, 240);
        config.particlePercent = clamp(config.particlePercent, 25, 100);
        config.particleSafeRadius = clamp(config.particleSafeRadius, 0, 32);
        config.entityDistance = clamp(config.entityDistance, 48, 256);
        config.blockEntityDistance = clamp(config.blockEntityDistance, 32, 192);
        config.maxRenderDistanceReduction = clamp(config.maxRenderDistanceReduction, 0, 4);
        config.minimumRenderDistance = clamp(config.minimumRenderDistance, 2, 16);
        config.idleFps = config.idleFps <= 15 ? 15 : 30;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
