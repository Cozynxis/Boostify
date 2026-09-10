package dev.cozynxis.boostify.mixin.client;

import dev.cozynxis.boostify.client.compat.Compatibility;
import dev.cozynxis.boostify.client.config.BoostifyConfig;
import dev.cozynxis.boostify.client.config.ConfigManager;
import dev.cozynxis.boostify.client.perf.AdaptiveController;
import dev.cozynxis.boostify.client.perf.BoostifyStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public final class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender", at = @At("RETURN"), cancellable = true)
    private void boostify$distanceCull(
        Entity entity, Frustum frustum, double camX, double camY, double camZ,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (!Boolean.TRUE.equals(cir.getReturnValue())) return;

        BoostifyConfig cfg = ConfigManager.get();
        if (!cfg.enabled || !cfg.entityDistanceCulling) return;
        if (cfg.compatibilityMode && !Compatibility.shouldRunEntityCulling()) return;

        Minecraft mc = Minecraft.getInstance();
        if (entity == mc.getCameraEntity() || entity instanceof Player) return;

        double range = cfg.entityDistance * AdaptiveController.entityDistanceMultiplier();
        double dx = entity.getX() - camX;
        double dy = entity.getY() - camY;
        double dz = entity.getZ() - camZ;
        if (dx * dx + dy * dy + dz * dz > range * range) {
            BoostifyStats.entitySkipped();
            cir.setReturnValue(false);
        }
    }
}
