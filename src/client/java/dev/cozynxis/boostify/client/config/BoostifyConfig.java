package dev.cozynxis.boostify.client.config;

public final class BoostifyConfig {
    public boolean enabled = true;
    public boolean adaptiveOptimizer = true;
    public int targetFps = 60;

    public boolean particleOptimizer = true;
    public int particlePercent = 65;
    public int particleSafeRadius = 10;

    public boolean entityDistanceCulling = true;
    public int entityDistance = 128;

    public boolean blockEntityDistanceCulling = true;
    public int blockEntityDistance = 80;

    public boolean dynamicRenderDistance = true;
    public int maxRenderDistanceReduction = 2;
    public int minimumRenderDistance = 6;

    public boolean idleFpsLimiter = true;
    public int idleFps = 30;

    public boolean overlayEnabled = false;
    public boolean compatibilityMode = true;

    public void applyPerformancePreset() {
        enabled = true;
        adaptiveOptimizer = true;
        targetFps = 60;
        particleOptimizer = true;
        particlePercent = 60;
        particleSafeRadius = 10;
        entityDistanceCulling = true;
        entityDistance = 112;
        blockEntityDistanceCulling = true;
        blockEntityDistance = 72;
        dynamicRenderDistance = true;
        maxRenderDistanceReduction = 2;
        minimumRenderDistance = 6;
        idleFpsLimiter = true;
        idleFps = 30;
        compatibilityMode = true;
    }

    public void resetBalancedDefaults() {
        enabled = true;
        adaptiveOptimizer = true;
        targetFps = 60;
        particleOptimizer = true;
        particlePercent = 65;
        particleSafeRadius = 10;
        entityDistanceCulling = true;
        entityDistance = 128;
        blockEntityDistanceCulling = true;
        blockEntityDistance = 80;
        dynamicRenderDistance = true;
        maxRenderDistanceReduction = 2;
        minimumRenderDistance = 6;
        idleFpsLimiter = true;
        idleFps = 30;
        overlayEnabled = false;
        compatibilityMode = true;
    }
}
