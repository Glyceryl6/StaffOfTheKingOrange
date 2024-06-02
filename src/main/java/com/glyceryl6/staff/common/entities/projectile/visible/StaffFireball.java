package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class StaffFireball extends SmallFireball {

    public StaffFireball(EntityType<? extends StaffFireball> type, Level level) {
        super(type, level);
    }

    public StaffFireball(Level level, LivingEntity shooter, double offsetX, double offsetY, double offsetZ) {
        super(level, shooter, offsetX, offsetY, offsetZ);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntityTypes.STAFF_FIREBALL.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getOwner();
            int i = entity.getRemainingFireTicks();
            entity.igniteForSeconds(5);
            if (entity.hurt(this.damageSources().fireball(this, entity1), 5.0F)) {
                entity.setRemainingFireTicks(i);
            } else if (entity1 instanceof LivingEntity livingEntity) {
                this.doEnchantDamageEffects(livingEntity, entity);
            }
        }
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

}