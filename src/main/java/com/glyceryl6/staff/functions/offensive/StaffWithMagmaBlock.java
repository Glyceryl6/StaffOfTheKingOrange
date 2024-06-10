package com.glyceryl6.staff.functions.offensive;

import com.glyceryl6.staff.common.entities.projectile.visible.StaffFireball;
import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StaffWithMagmaBlock implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            StaffFireball fireball = new StaffFireball(level, player, 0.0D, 0.0D, 0.0D);
            fireball.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            fireball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.levelEvent(1018, fireball.blockPosition(), 0);
            level.addFreshEntity(fireball);
        }
    }

    @Override
    public void attackEntity(Level level, Player player, Entity target) {
        if (!target.fireImmune()) {
            target.hurt(level.damageSources().onFire(), 5.0F);
            target.setRemainingFireTicks(200);
        }
    }

}