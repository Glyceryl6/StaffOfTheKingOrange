package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.common.entities.projectile.invisible.BoneMeal;
import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class StaffWithBoneBlock implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            BoneMeal boneMeal = new BoneMeal(player, 0.0D, 0.0D, 0.0D, level);
            boneMeal.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 1.0F);
            level.addFreshEntity(boneMeal);
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        this.useTick(context.getLevel(), context.getPlayer(), context.getItemInHand());
    }

}