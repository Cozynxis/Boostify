package dev.cozynxis.boostify.client.perf;

import dev.cozynxis.boostify.client.config.BoostifyConfig;
import dev.cozynxis.boostify.client.config.ConfigManager;
import net.minecraft.client.Minecraft;

public final class AdaptiveController {
    private static int ticks;
    private static int pressure;

    private static Integer originalIdleFpsLimit;
    private static Integer baseRenderDistance;
    private static Integer lastAppliedRenderDistance;

    private AdaptiveController() {}

    public static void tick(Minecraft client) {
        BoostifyConfig cfg = ConfigManager.get();
        if (!cfg.enabled) {
            pressure = 0;
            restoreTemporaryVanillaOptions(client);
            return;
        }

        updateIdleLimit(client, cfg);

        if (++ticks < 20) return;
        ticks = 0;

        updatePressure(client, cfg);
        updateRenderDistance(client, cfg);
    }

    private static void updatePressure(Minecraft client, BoostifyConfig cfg) {
        if (!cfg.adaptiveOptimizer || client.level == null) {
            pressure = 0;
            return;
        }

        int fps = Math.max(1, client.getFps());
        if (fps < cfg.targetFps * 0.55) pressure = 3;
        else if (fps < cfg.targetFps * 0.72) pressure = 2;
        else if (fps < cfg.targetFps * 0.88) pressure = 1;
        else if (fps > cfg.targetFps * 1.08) pressure = Math.max(0, pressure - 1);
    }

    private static void updateIdleLimit(Minecraft client, BoostifyConfig cfg) {
        if (!cfg.idleFpsLimiter) {
            restoreIdleLimit(client);
            return;
        }

        if (!client.isWindowActive()) {
            if (originalIdleFpsLimit == null) {
                originalIdleFpsLimit = client.options.framerateLimit().get();
            }
            int wanted = Math.min(originalIdleFpsLimit, cfg.idleFps);
            if (client.options.framerateLimit().get() != wanted) {
                client.options.framerateLimit().set(wanted);
            }
        } else {
            restoreIdleLimit(client);
        }
    }

    private static void updateRenderDistance(Minecraft client, BoostifyConfig cfg) {
        if (!cfg.dynamicRenderDistance || !cfg.adaptiveOptimizer || client.level == null) {
            restoreRenderDistance(client);
            return;
        }

        int current = client.options.renderDistance().get();
        if (baseRenderDistance == null) {
            baseRenderDistance = current;
        } else if (lastAppliedRenderDistance != null && current != lastAppliedRenderDistance) {
            // The player changed render distance themselves. Respect that new choice as the baseline.
            baseRenderDistance = current;
        }

        int reduction = Math.min(cfg.maxRenderDistanceReduction, pressure);
        int target = Math.max(cfg.minimumRenderDistance, baseRenderDistance - reduction);
        if (current != target) {
            client.options.renderDistance().set(target);
        }
        lastAppliedRenderDistance = target;
    }

    public static void restoreTemporaryVanillaOptions(Minecraft client) {
        restoreIdleLimit(client);
        restoreRenderDistance(client);
    }

    private static void restoreIdleLimit(Minecraft client) {
        if (originalIdleFpsLimit != null) {
            client.options.framerateLimit().set(originalIdleFpsLimit);
            originalIdleFpsLimit = null;
        }
    }

    private static void restoreRenderDistance(Minecraft client) {
        if (baseRenderDistance != null) {
            client.options.renderDistance().set(baseRenderDistance);
        }
        baseRenderDistance = null;
        lastAppliedRenderDistance = null;
    }

    public static int pressure() {
        return pressure;
    }

    public static double particleMultiplier() {
        double base = ConfigManager.get().particlePercent / 100.0;
        return switch (pressure) {
            case 3 -> base * 0.62;
            case 2 -> base * 0.75;
            case 1 -> base * 0.88;
            default -> base;
        };
    }

    public static double entityDistanceMultiplier() {
        return switch (pressure) {
            case 3 -> 0.68;
            case 2 -> 0.78;
            case 1 -> 0.90;
            default -> 1.0;
        };
    }
}
