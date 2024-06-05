package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.common.entities.projectile.invisible.BoneMeal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

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
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos relative = clickedPos.relative(context.getClickedFace());
        if (BoneMeal.applyBoneMeal(level, clickedPos)) {
            if (player != null && !level.isClientSide) {
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                level.levelEvent(1505, clickedPos, 15);
            }
        } else {
            BlockState state = level.getBlockState(clickedPos);
            boolean flag = state.isFaceSturdy(level, clickedPos, context.getClickedFace());
            if (flag && BoneMeal.growWaterPlant(level, relative, context.getClickedFace())) {
                if (player != null && !level.isClientSide) {
                    player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                    level.levelEvent(1505, relative, 15);
                }
            }
        }
    }

}