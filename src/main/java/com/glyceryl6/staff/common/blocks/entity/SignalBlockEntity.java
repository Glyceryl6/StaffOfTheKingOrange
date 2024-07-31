package com.glyceryl6.staff.common.blocks.entity;

import com.glyceryl6.staff.registry.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SignalBlockEntity extends BlockEntity {

    public int removeCountdown;

    public SignalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.SIGNAL_BLOCK.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SignalBlockEntity blockEntity) {
        if (!level.isClientSide && --blockEntity.removeCountdown <= 0) {
            level.removeBlock(pos, Boolean.FALSE);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.removeCountdown = tag.getInt("RemoveCountdown");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("RemoveCountdown", this.removeCountdown);
    }

}