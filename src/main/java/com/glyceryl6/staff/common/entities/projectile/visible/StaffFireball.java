package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.registry.KOEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class StaffFireball extends SmallFireball {

    public StaffFireball(EntityType<? extends StaffFireball> type, Level level) {
        super(type, level);
    }

    public StaffFireball(Level level, LivingEntity shooter, Vec3 movement) {
        super(level, shooter, movement);
    }

    @Override
    public EntityType<?> getType() {
        return KOEntityTypes.STAFF_FIREBALL.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.level() instanceof ServerLevel serverLevel) {
            Entity entity = result.getEntity();
            Entity entity1 = this.getOwner();
            int i = entity.getRemainingFireTicks();
            entity.igniteForSeconds(5);
            DamageSource source = this.damageSources().fireball(this, entity1);
            if (entity.hurt(source, 5.0F)) {
                entity.setRemainingFireTicks(i);
            } else if (entity1 instanceof LivingEntity) {
                EnchantmentHelper.doPostAttackEffects(serverLevel, entity, source);
            }
        }
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

}