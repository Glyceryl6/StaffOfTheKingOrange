package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.common.entities.FakeBlock;
import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.utils.StaffSpecialUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StaffWithLapisBlock implements INormalStaffFunction {

    @Override
    public void use(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide && player.isFallFlying()) {
            Vec3 vec31 = player.getLookAngle();
            Vec3 vec32 = player.getDeltaMovement();
            double x = vec31.x * 0.1D + (vec31.x * 1.5D - vec32.x) * 0.5D;
            double y = vec31.y * 0.1D + (vec31.y * 1.5D - vec32.y) * 0.5D;
            double z = vec31.z * 0.1D + (vec31.z * 1.5D - vec32.z) * 0.5D;
            player.setDeltaMovement(vec32.add(x, y, z));
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        float pitch = level.random.nextFloat() * 0.1F + 0.9F;
        level.playSound(null, pos,
                SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundSource.BLOCKS, 1.0F, pitch);
        if (!level.isClientSide && !state.hasBlockEntity()) {
            FakeBlock fakeBlock = new FakeBlock(level, pos);
            level.removeBlock(pos, Boolean.FALSE);
            fakeBlock.setBlockState(state);
            level.addFreshEntity(fakeBlock);
        }
    }

    @Override
    public void useOnEntity(Player player, InteractionHand hand, Entity target) {
        StaffSpecialUtils.setEntityGlint(player.level(), target);
    }

}