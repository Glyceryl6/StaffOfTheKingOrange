package com.glyceryl6.staff.functions.other;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.common.entities.projectile.invisible.IceBomb;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public record StaffWithIce(Block block) implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            IceBomb iceBomb = new IceBomb(player, 0.0D, 0.0D, 0.0D, level);
            iceBomb.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            iceBomb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 0.0F);
            iceBomb.setDeltaMovement(player.getViewVector(1.0F).scale(2.0F));
            iceBomb.setBlockState(this.block.defaultBlockState());
            level.addFreshEntity(iceBomb);
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            BlockPos pos = context.getClickedPos();
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPos pos2 = pos.offset(x, y, z);
                        if (!level.getBlockState(pos2).isAir()) {
                            level.setBlockAndUpdate(pos2, this.block.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

}