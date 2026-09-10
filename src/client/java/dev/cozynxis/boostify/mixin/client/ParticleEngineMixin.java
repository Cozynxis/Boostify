package dev.cozynxis.boostify.mixin.client;

import dev.cozynxis.boostify.client.config.BoostifyConfig;
import dev.cozynxis.boostify.client.config.ConfigManager;
import dev.cozynxis.boostify.client.perf.AdaptiveController;
import dev.cozynxis.boostify.client.perf.BoostifyStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(ParticleEngine.class)
public final class ParticleEngineMixin {
    @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
    private void boostify$reduceFarParticles(
        ParticleOptions options,
        double x, double y, double z,
        double xSpeed, double ySpeed, double zSpeed,
        CallbackInfoReturnable<Particle> cir
    ) {
        BoostifyConfig cfg = ConfigManager.get();
        if (!cfg.enabled || !cfg.particleOptimizer || cfg.particlePercent >= 100) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        double dx = x - mc.player.getX();
        double dy = y - mc.player.getY();
        double dz = z - mc.player.getZ();
        double safeSq = cfg.particleSafeRadius * (double) cfg.particleSafeRadius;
        if (dx * dx + dy * dy + dz * dz <= safeSq) return;

        double keepChance = Math.max(0.20, Math.min(1.0, AdaptiveController.particleMultiplier()));
        if (ThreadLocalRandom.current().nextDouble() > keepChance) {
            BoostifyStats.particleSkipped();
            cir.setReturnValue(null);
        }
    }
}
