package com.glyceryl6.staff.common.entities.projectile.invisible;

import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

/** @noinspection deprecation*/
public class IceBomb extends AbstractInvisibleProjectile {

    private BlockState blockState = Blocks.ICE.defaultBlockState();

    public IceBomb(EntityType<? extends IceBomb> type, Level level) {
        super(type, level);
    }

    public IceBomb(LivingEntity shooter, double offsetX, double offsetY, double offsetZ, Level level) {
        super(ModEntityTypes.ICE_BOMB.get(), shooter, offsetX, offsetY, offsetZ, level);
    }

    @Nullable @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.SNOWFLAKE;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("BlockState", NbtUtils.writeBlockState(this.blockState));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        HolderLookup<Block> blockGetter = this.level().holderLookup(Registries.BLOCK);
        this.blockState = NbtUtils.readBlockState(blockGetter, compound.getCompound("BlockState"));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Level level = this.level();
        if (result.getEntity() instanceof LivingEntity entity) {
            EntityDimensions dimensions = entity.getDimensions(Pose.STANDING);
            int x = Mth.ceil(dimensions.width() / 2.0F);
            int y = Mth.ceil(dimensions.height() / 2.0F + 1.0F);
            BlockPos pos = entity.blockPosition();
            BlockPos.withinManhattan(pos, x, y, x).forEach(tempPos -> {
                if (!level.getBlockState(tempPos).isSolid()) {
                    level.setBlockAndUpdate(tempPos, this.blockState);
                }
            });
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Level level = this.level();
        if (!level.isClientSide) {
            BlockPos pos = result.getBlockPos();
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        BlockPos pos2 = pos.offset(x, y, z);
                        BlockState state = level.getBlockState(pos2);
                        if (state.is(Blocks.WATER)) {
                            level.setBlockAndUpdate(pos2, Blocks.ICE.defaultBlockState());
                        } else if (state.is(Blocks.LAVA)) {
                            level.setBlockAndUpdate(pos2, Blocks.OBSIDIAN.defaultBlockState());
                        } else if (!state.isAir() && !state.is(BlockTags.ICE)) {
                            level.setBlockAndUpdate(pos2, this.blockState);
                        }
                    }
                }
            }
        }
    }

}