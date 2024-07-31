package com.glyceryl6.staff.functions.destructive;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class StaffWithDiamondBlock implements INormalStaffFunction {

    @Override
    public boolean enableUse() {
        return false;
    }

    @Override
    public boolean enableUseTick() {
        return false;
    }

    @Override
    public void attackBlock(Level level, Player player, BlockPos pos) {
        BlockPos pos1 = pos.relative(player.getDirection(), 7);
        for (int x = -7; x <= 7; x++) {
            for (int y = -7; y <= 7; y++) {
                for (int z = -7; z <= 7; z++) {
                    BlockPos pos2 = pos1.offset(x, y, z);
                    level.destroyBlock(pos2, Boolean.TRUE);
                }
            }
        }
    }
    
}