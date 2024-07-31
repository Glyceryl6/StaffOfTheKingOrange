package com.glyceryl6.staff.functions.offensive;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class StaffWithLightningRod implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        HitResult result = player.pick((64.0D), (0.0F), Boolean.FALSE);
        if (result instanceof BlockHitResult blockHitResult) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState state = level.getBlockState(blockPos);
            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            if (lightningBolt != null && !state.isAir() && !level.isClientSide) {
                lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                level.addFreshEntity(lightningBolt);
            }
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        if (!level.isClientSide && lightningBolt != null) {
            lightningBolt.moveTo(Vec3.atBottomCenterOf(clickedPos));
            level.addFreshEntity(lightningBolt);
        }
    }

    @Override
    public void useOnEntity(Player player, InteractionHand hand, Entity target) {
        this.attackEntity(player.level(), player, target);
    }

    @Override
    public void attackEntity(Level level, Player player, Entity target) {
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        if (!level.isClientSide && lightningBolt != null) {
            lightningBolt.moveTo(target.position());
            level.addFreshEntity(lightningBolt);
        }
    }

}