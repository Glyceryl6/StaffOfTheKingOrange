package com.glyceryl6.staff.functions.other;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class StaffWithSnowBlock implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        float pitch = 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, pitch);
        if (!level.isClientSide) {
            Snowball snowball = new Snowball(level, player);
            snowball.setItem(Items.SNOWBALL.getDefaultInstance());
            snowball.setPos(player.getRandomX(0.5D), player.getY(1.0D) - 0.5F, player.getRandomZ(0.5D));
            snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(snowball);
        }
    }

}