package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.common.entities.projectile.invisible.Enchant;
import com.glyceryl6.staff.utils.StaffSpecialUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class StaffWithEnchantingTable implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            Enchant enchant = new Enchant(player, 0.0D, 0.0D, 0.0D, level);
            enchant.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            enchant.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0.0F);
            level.addFreshEntity(enchant);
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            StaffSpecialUtils.setRandomEnchantment(level, context.getClickedPos());
        }
    }

}