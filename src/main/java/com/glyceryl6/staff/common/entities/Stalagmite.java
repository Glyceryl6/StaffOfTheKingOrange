package com.glyceryl6.staff.common.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.UUID;

public class Stalagmite extends Entity {

    private int lifeTicks = 40;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public Stalagmite(EntityType<? extends Stalagmite> type, Level level) {
        super(type, level);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUUID = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null) {
            if (this.level() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(this.ownerUUID);
                if (entity instanceof LivingEntity livingEntity) {
                    this.owner = livingEntity;
                }
            }
        }

        return this.owner;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    public void tick() {
        super.tick();
        --this.lifeTicks;
        if (this.lifeTicks < 0) {
            this.discard();
        } else {
            if (this.lifeTicks > 31) {
                this.setPos(this.position().add(0.0D, 0.5D, 0.0D));
            }

            AABB aabb = this.getBoundingBox().inflate(0.4F, 1.0F, 0.4F);
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, aabb)) {
                this.dealDamageTo(entity);
            }
        }
    }

    private void dealDamageTo(LivingEntity target) {
        LivingEntity livingEntity = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && target != livingEntity) {
            DamageSource source = this.damageSources().stalagmite();
            if (livingEntity == null) {
                target.hurt(source, 6.0F);
            } else {
                if (livingEntity.isAlliedTo(target)) {
                    return;
                }

                if (target.hurt(source, 6.0F) && this.level() instanceof ServerLevel serverLevel) {
                    EnchantmentHelper.doPostAttackEffects(serverLevel, target, source);
                }
            }
        }
    }

}