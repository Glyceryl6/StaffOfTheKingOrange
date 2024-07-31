package com.glyceryl6.staff.common.entities;

import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FakeBlock extends Entity {

    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE_ID = SynchedEntityData.defineId(FakeBlock.class, EntityDataSerializers.BLOCK_STATE);

    public FakeBlock(EntityType<?> type, Level level) {
        super(type, level);
    }

    public FakeBlock(Level level, BlockPos pos) {
        this(ModEntityTypes.FAKE_BLOCK.get(), level);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_BLOCK_STATE_ID, Blocks.SAND.defaultBlockState());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.put("BlockState", NbtUtils.writeBlockState(this.getBlockState()));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        HolderLookup<Block> blockGetter = this.level().holderLookup(Registries.BLOCK);
        this.setBlockState(NbtUtils.readBlockState(blockGetter, compound.getCompound("BlockState")));
        if (this.getBlockState().isAir()) {
            this.setBlockState(Blocks.SAND.defaultBlockState());
        }
    }

    @Override
    public boolean skipAttackInteraction(Entity entity) {
        return entity instanceof Player player && !this.level().mayInteract(player, this.blockPosition());
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        if (flag || source.isCreativePlayer() || source.is(DamageTypes.EXPLOSION)) {
            this.kill();
            return true;
        } else {
            return super.hurt(source, amount);
        }
    }

    @Override
    public void setPos(double x, double y, double z) {
        double x1 = Mth.floor(x) + 0.5D;
        double y1 = Mth.floor((y + 0.5D));
        double z1 = Mth.floor(z) + 0.5D;
        super.setPos(x1, y1, z1);
        this.xo = x1;
        this.yo = y1;
        this.zo = z1;
    }

    public BlockState getBlockState() {
        return this.entityData.get(DATA_BLOCK_STATE_ID);
    }

    public void setBlockState(BlockState state) {
        this.entityData.set(DATA_BLOCK_STATE_ID, state);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

}