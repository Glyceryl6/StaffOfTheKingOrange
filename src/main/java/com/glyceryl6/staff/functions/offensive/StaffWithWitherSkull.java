package com.glyceryl6.staff.functions.offensive;

import com.glyceryl6.staff.common.entities.projectile.visible.StaffWitherSkull;
import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StaffWithWitherSkull implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            WitherSkull witherSkull = new StaffWitherSkull(level, player, 0.0D, 0.0D, 0.0D);
            witherSkull.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            witherSkull.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            witherSkull.setDangerous(level.random.nextFloat() < 0.1F);
            witherSkull.setOwner(player);
            BlockPos pos = witherSkull.blockPosition();
            level.levelEvent(1024, pos, 0);
            level.addFreshEntity(witherSkull);
        }
    }

    @Override
    public void attackEntity(Level level, Player player, Entity target) {
        if (!level.isClientSide && target instanceof LivingEntity livingEntity) {
            if (livingEntity.hurt(player.damageSources().magic(), 8.0F)) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 1200, 1));
            }
        }
    }

}