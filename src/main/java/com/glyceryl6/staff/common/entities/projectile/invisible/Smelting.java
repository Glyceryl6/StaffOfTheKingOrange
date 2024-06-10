package com.glyceryl6.staff.common.entities.projectile.invisible;

import com.glyceryl6.staff.registry.ModEntityTypes;
import com.glyceryl6.staff.utils.StaffSpecialUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class Smelting extends AbstractInvisibleProjectile {

    public RecipeType<? extends AbstractCookingRecipe> recipeType = new RecipeType<>() {};

    public Smelting(EntityType<? extends Smelting> type, Level level) {
        super(type, level);
    }

    public Smelting(LivingEntity shooter, double offsetX, double offsetY, double offsetZ, Level level) {
        super(ModEntityTypes.SMELTING.get(), shooter, offsetX, offsetY, offsetZ, level);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("RecipeType", this.recipeType.toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.recipeType = RecipeType.register(compound.getString("RecipeType"));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 15; i++) {
                double d0 = this.getX() + this.random.nextDouble();
                double d2 = this.getZ() + this.random.nextDouble();
                this.level().addParticle(ParticleTypes.LAVA, d0, this.getY(), d2, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        Level level = this.level();
        if (!level.isClientSide) {
            if (result instanceof BlockHitResult hit) {
                BlockPos blockPos = hit.getBlockPos();
                StaffSpecialUtils.smeltingItemOrBlock(this.recipeType, blockPos, level);
            }

            level.broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

}