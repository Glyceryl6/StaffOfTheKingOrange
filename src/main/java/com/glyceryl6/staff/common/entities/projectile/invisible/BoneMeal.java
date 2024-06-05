package com.glyceryl6.staff.common.entities.projectile.invisible;

import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BoneMeal extends AbstractInvisibleProjectile {

    public BoneMeal(EntityType<? extends BoneMeal> type, Level level) {
        super(type, level);
    }

    public BoneMeal(LivingEntity shooter, double offsetX, double offsetY, double offsetZ, Level level) {
        super(ModEntityTypes.BONE_MEAL.get(), shooter, offsetX, offsetY, offsetZ, level);
    }

    @Nullable @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.HAPPY_VILLAGER;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        Level level = this.level();
        Direction clickedFace = result.getDirection();
        BlockPos clickedPos = result.getBlockPos();
        BlockPos relative = clickedPos.relative(clickedFace);
        if (applyBoneMeal(level, clickedPos)) {
            if (!level.isClientSide) {
                level.levelEvent(1505, clickedPos, 15);
                this.discard();
            }
        } else {
            BlockState blockState = level.getBlockState(clickedPos);
            boolean flag = blockState.isFaceSturdy(level, clickedPos, clickedFace);
            if (flag && growWaterPlant(level, relative, clickedFace)) {
                if (!level.isClientSide) {
                    level.levelEvent(1505, relative, 15);
                    this.discard();
                }
            }
        }
    }

    public static boolean applyBoneMeal(Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        if (blockState.getBlock() instanceof BonemealableBlock block && block.isValidBonemealTarget(level, pos, blockState)) {
            if (level instanceof ServerLevel serverLevel && block.isBonemealSuccess(level, level.random, pos, blockState)) {
                block.performBonemeal(serverLevel, level.random, pos, blockState);
            }

            return true;
        }

        return false;
    }

    public static boolean growWaterPlant(Level level, BlockPos pos, @Nullable Direction clickedSide) {
        if (level.getBlockState(pos).is(Blocks.WATER) && level.getFluidState(pos).getAmount() == 8) {
            if (level instanceof ServerLevel serverLevel) {
                RandomSource random = level.getRandom();
                label78:
                for (int i = 0; i < 128; i++) {
                    BlockPos blockPos = pos;
                    BlockState blockState = Blocks.SEAGRASS.defaultBlockState();
                    for (int j = 0; j < i / 16; j++) {
                        int dx = random.nextInt(3) - 1;
                        int dy = (random.nextInt(3) - 1) * random.nextInt(3) / 2;
                        int dz = random.nextInt(3) - 1;
                        blockPos = blockPos.offset(dx, dy, dz);
                        if (level.getBlockState(blockPos).isCollisionShapeFullBlock(level, blockPos)) {
                            continue label78;
                        }
                    }

                    if (level.getBiome(blockPos).is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
                        if (i == 0 && clickedSide != null && clickedSide.getAxis().isHorizontal()) {
                            blockState = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.WALL_CORALS, level.random)
                                    .map(holder -> holder.value().defaultBlockState()).orElse(blockState);
                            if (blockState.hasProperty(BaseCoralWallFanBlock.FACING)) {
                                blockState = blockState.setValue(BaseCoralWallFanBlock.FACING, clickedSide);
                            }
                        } else if (random.nextInt(4) == 0) {
                            blockState = BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.UNDERWATER_BONEMEALS, level.random)
                                    .map(holder -> holder.value().defaultBlockState()).orElse(blockState);
                        }
                    }

                    if (blockState.is(BlockTags.WALL_CORALS, base -> base.hasProperty(BaseCoralWallFanBlock.FACING))) {
                        for (int k = 0; !blockState.canSurvive(level, blockPos) && k < 4; k++) {
                            blockState = blockState.setValue(BaseCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                        }
                    }

                    if (blockState.canSurvive(level, blockPos)) {
                        BlockState blockState1 = level.getBlockState(blockPos);
                        if (blockState1.is(Blocks.WATER) && level.getFluidState(blockPos).getAmount() == 8) {
                            level.setBlock(blockPos, blockState, Block.UPDATE_ALL);
                        } else if (blockState1.getBlock() instanceof BonemealableBlock bonemealableBlock && random.nextInt(10) == 0) {
                            bonemealableBlock.performBonemeal(serverLevel, random, blockPos, blockState1);
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

}