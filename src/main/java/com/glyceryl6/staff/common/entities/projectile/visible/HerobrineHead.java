package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HerobrineHead extends AbstractPlayerHead {

    public HerobrineHead(EntityType<? extends HerobrineHead> type, Level level) {
        super(type, level);
    }

    public HerobrineHead(Level level, LivingEntity shooter, Vec3 movement) {
        super(ModEntityTypes.HEROBRINE_HEAD.get(), level, shooter, movement);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return true;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        Level level = this.level();
        if (!level.isClientSide) {
            level.explode(this, this.getX(), this.getY(), this.getZ(), 4.0F, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

}