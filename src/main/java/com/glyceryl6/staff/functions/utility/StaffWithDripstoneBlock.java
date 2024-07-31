package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StaffWithDripstoneBlock implements INormalStaffFunction {

    @Override
    public void use(Level level, Player player, ItemStack stack) {

    }

    @Override
    public void attackEntity(Level level, Player player, Entity target) {
        if (!level.isClientSide && target instanceof LivingEntity entity && entity.onGround()) {

        }
    }

}