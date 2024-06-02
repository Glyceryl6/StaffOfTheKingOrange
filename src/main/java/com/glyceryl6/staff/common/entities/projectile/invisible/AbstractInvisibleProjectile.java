package com.glyceryl6.staff.common.entities.projectile.invisible;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class AbstractInvisibleProjectile extends AbstractHurtingProjectile {

    public AbstractInvisibleProjectile(EntityType<? extends AbstractInvisibleProjectile> type, Level level) {
        super(type, level);
    }

    public AbstractInvisibleProjectile(EntityType<? extends AbstractInvisibleProjectile> type, LivingEntity shooter, double offsetX, double offsetY, double offsetZ, Level level) {
        super(type, shooter, offsetX, offsetY, offsetZ, level);
    }

    @Override
    public void push(double x, double y, double z) {}

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected float getLiquidInertia() {
        return this.getInertia();
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        ParticleOptions particle = this.getTrailParticle();
        if (this.level().isClientSide && particle != null) {
            for (int i = 0; i < 10; i++) {
                double x = this.getRandomX(1.0D);
                double y = this.getRandomY();
                double z = this.getRandomZ(1.0D);
                this.level().addParticle(particle, x, y, z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

}