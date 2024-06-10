package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.common.entities.projectile.invisible.Smelting;
import com.glyceryl6.staff.utils.StaffSpecialUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record StaffWithFurnace(RecipeType<? extends AbstractCookingRecipe> recipeType) implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            Smelting smelting = new Smelting(player, 0.0D, 0.0D, 0.0D, level);
            smelting.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            smelting.setDeltaMovement(player.getViewVector(1.0F).scale(2.0F));
            smelting.recipeType = this.recipeType;
            level.addFreshEntity(smelting);
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        this.startSmelting(context.getLevel(), context.getClickedPos());
    }

    @Override
    public void attackBlock(Level level, Player player, BlockPos pos) {
        this.startSmelting(level, pos);
    }

    private void startSmelting(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            StaffSpecialUtils.smeltingItemOrBlock(this.recipeType, pos, level);
        } else {
            for (int i = 0; i < 15; i++) {
                double d0 = pos.getX() + level.random.nextDouble();
                double d2 = pos.getZ() + level.random.nextDouble();
                level.addParticle(ParticleTypes.LAVA, d0, pos.getY(), d2, 0.0, 0.0, 0.0);
            }
        }
    }

}