package com.glyceryl6.staff.common.entities.projectile.invisible;

import com.glyceryl6.staff.common.blocks.entity.SignalBlockEntity;
import com.glyceryl6.staff.registry.ModBlocks;
import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class Signal extends AbstractInvisibleProjectile {

    public Signal(EntityType<? extends Signal> type, Level level) {
        super(type, level);
    }

    public Signal(LivingEntity shooter, double offsetX, double offsetY, double offsetZ, Level level) {
        super(ModEntityTypes.SIGNAL.get(), shooter, offsetX, offsetY, offsetZ, level);
    }

    @Nullable @Override
    protected ParticleOptions getTrailParticle() {
        return DustParticleOptions.REDSTONE;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide && result.getEntity() instanceof LivingEntity entity) {
            entity.hurt(this.damageSources().magic(), 2.0F);
            entity.invulnerableTime = 0;
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Level level = this.level();
        if (!level.isClientSide) {
            BlockPos blockPos = this.blockPosition();
            BlockPos.withinManhattan(blockPos, 1, 1, 1).forEach(pos -> {
                if (level.getBlockState(pos).isAir()) {
                    level.setBlockAndUpdate(pos, ModBlocks.SIGNAL_BLOCK.get().defaultBlockState());
                    if (level.getBlockEntity(pos) instanceof SignalBlockEntity blockEntity) {
                        blockEntity.removeCountdown = 2;
                    }
                }
            });
        }
    }

}