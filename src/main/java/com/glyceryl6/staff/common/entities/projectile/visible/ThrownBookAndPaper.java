package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.registry.KODamageTypes;
import com.glyceryl6.staff.registry.KOEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownBookAndPaper extends AbstractThrownItem {

    public ThrownBookAndPaper(EntityType<? extends AbstractThrownItem> type, Level level) {
        super(type, level);
    }

    public ThrownBookAndPaper(LivingEntity shooter, Vec3 movement, Level level) {
        super(KOEntityTypes.THROWN_BOOK_AND_PAPER.get(), shooter, movement, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity livingEntity && !this.level().isClientSide) {
            livingEntity.hurt(this.damageSources().source(KODamageTypes.KNOWLEDGE, (this), this.getOwner()), this.damageAmount);
            livingEntity.invulnerableTime = 0;
        }
    }

}