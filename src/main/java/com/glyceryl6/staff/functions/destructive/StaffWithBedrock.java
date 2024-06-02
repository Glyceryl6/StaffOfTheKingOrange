package com.glyceryl6.staff.functions.destructive;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StaffWithBedrock implements INormalStaffFunction {

    @Override
    public boolean enableUseTick() {
        return false;
    }

    @Override
    public boolean canPlaceBlock(UseOnContext context) {
        return true;
    }

    @Override
    public void attackBlock(Level level, Player player, BlockPos pos) {
        Direction direction = player.getDirection();
        BlockPos pos1 = pos.relative(direction, (5));
        if (!level.isClientSide) {
            RandomSource random = level.random;
            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    BlockPos pos2 = pos1.offset(x, 0, z);
                    BlockState state = level.getBlockState(pos2);
                    level.removeBlock(pos2, Boolean.FALSE);
                    double d0 = random.triangle((double)direction.getStepX(), 0.0D);
                    double d2 = random.triangle((double)direction.getStepZ(), 0.0D);
                    FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, pos2, state);
                    fallingBlock.setDeltaMovement(new Vec3(d0, 0.5D, d2));
                    level.addFreshEntity(fallingBlock);
                }
            }
        }
    }

}