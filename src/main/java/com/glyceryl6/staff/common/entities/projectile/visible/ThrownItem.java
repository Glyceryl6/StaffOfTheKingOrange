package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownItem extends Fireball {

    public float damageAmount;
    
    public ThrownItem(EntityType<? extends ThrownItem> type, Level level) {
        super(type, level);
    }

    public ThrownItem(LivingEntity shooter, double offsetX, double offsetY, double offsetZ, Level level) {
        super(ModEntityTypes.THROWN_ITEM.get(), shooter, offsetX, offsetY, offsetZ, level);
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("damage_amount", this.damageAmount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.damageAmount = compound.getFloat("damage_amount");
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particleOptions = new ItemParticleOption(ParticleTypes.ITEM, this.getItem());
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particleOptions, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity livingEntity && !this.level().isClientSide) {
            livingEntity.hurt(this.damageSources().thrown((this), this.getOwner()), this.damageAmount);
            livingEntity.invulnerableTime = 0;
        }
    }

}