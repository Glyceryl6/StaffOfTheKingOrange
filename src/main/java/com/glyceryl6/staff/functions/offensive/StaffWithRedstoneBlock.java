package com.glyceryl6.staff.functions.offensive;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.common.entities.projectile.invisible.Signal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class StaffWithRedstoneBlock implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            Signal signal = new Signal(player, 0.0D, 0.0D, 0.0D, level);
            signal.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            signal.setDeltaMovement(player.getViewVector(1.0F).scale(2.5F));
            level.addFreshEntity(signal);
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        this.useTick(context.getLevel(), context.getPlayer(), context.getItemInHand());
    }

}