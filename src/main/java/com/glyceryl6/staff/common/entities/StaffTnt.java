package com.glyceryl6.staff.common.entities;

import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class StaffTnt extends PrimedTnt {

    public StaffTnt(EntityType<? extends StaffTnt> type, Level level) {
        super(type, level);
    }

    public StaffTnt(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        super(level, x, y, z, owner);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntityTypes.STAFF_TNT.get();
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    public void tick() {
        this.applyGravity();
        this.setFuse(this.getFuse() - 1);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        if (this.horizontalCollision || this.verticalCollision) {
            this.discard();
            if (!this.level().isClientSide) {
                this.explode();
            } else {
                this.updateInWaterStateAndDoFluidPushing();
                if (this.level().isClientSide) {
                    this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

}