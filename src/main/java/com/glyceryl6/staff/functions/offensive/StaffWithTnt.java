package com.glyceryl6.staff.functions.offensive;

import com.glyceryl6.staff.common.entities.StaffTnt;
import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StaffWithTnt implements INormalStaffFunction {

    @Override
    public boolean enableUseOnBlock() {
        return false;
    }

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            StaffTnt tnt = new StaffTnt(level, player.getX(), player.getY(), player.getZ(), player);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TNT_PRIMED, SoundSource.NEUTRAL, 1.0f, 1.0f);
            tnt.setPos(player.getRandomX(0.5D), player.getY(0.5D) + 0.25F, player.getRandomZ(0.5D));
            tnt.setDeltaMovement(player.getViewVector(1.0F).scale(2.0F));
            tnt.setFuse(Short.MAX_VALUE);
            level.addFreshEntity(tnt);
        }
    }

}