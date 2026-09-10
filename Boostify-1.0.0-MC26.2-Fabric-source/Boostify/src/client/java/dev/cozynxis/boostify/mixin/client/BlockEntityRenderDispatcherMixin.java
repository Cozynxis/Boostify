package dev.cozynxis.boostify.mixin.client;

import dev.cozynxis.boostify.client.config.BoostifyConfig;
import dev.cozynxis.boostify.client.config.ConfigManager;
import dev.cozynxis.boostify.client.perf.AdaptiveController;
import dev.cozynxis.boostify.client.perf.BoostifyStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @Inject(method = "tryExtractRenderState", at = @At("HEAD"), cancellable = true)
    private <E extends BlockEntity, S extends BlockEntityRenderState> void boostify$distanceCullBlockEntities(
        E blockEntity,
        float partialTick,
        ModelFeatureRenderer.CrumblingOverlay overlay,
        boolean isGloballyRendered,
        CallbackInfoReturnable<S> cir
    ) {
        BoostifyConfig cfg = ConfigManager.get();
        if (!cfg.enabled || !cfg.blockEntityDistanceCulling || isGloballyRendered) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        BlockPos pos = blockEntity.getBlockPos();
        double range = cfg.blockEntityDistance * AdaptiveController.entityDistanceMultiplier();
        double dx = pos.getX() + 0.5 - mc.player.getX();
        double dy = pos.getY() + 0.5 - mc.player.getY();
        double dz = pos.getZ() + 0.5 - mc.player.getZ();
        if (dx * dx + dy * dy + dz * dz > range * range) {
            BoostifyStats.blockEntitySkipped();
            cir.setReturnValue(null);
        }
    }
}
