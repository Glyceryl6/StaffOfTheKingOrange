package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.registry.KOEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.awt.*;
import java.util.OptionalInt;

/** @noinspection deprecation*/
public class ThrowableBlock extends ThrowableProjectile {

    private static final EntityDataAccessor<OptionalInt> OWNER_ENTITY_ID = SynchedEntityData.defineId(ThrowableBlock.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
    private static final EntityDataAccessor<BlockState> BLOCK_STATE = SynchedEntityData.defineId(ThrowableBlock.class, EntityDataSerializers.BLOCK_STATE);
    private static final Vector3f LAPIS_PARTICLE_COLOR = Vec3.fromRGB24(Color.BLUE.getRGB()).toVector3f();
    @Nullable
    private EntityType<?> ownerType;

    public ThrowableBlock(EntityType<? extends ThrowableBlock> type, Level level) {
        super(type, level);
    }

    public ThrowableBlock(double x, double y, double z, LivingEntity entity) {
        super(KOEntityTypes.THROWABLE_BLOCK.get(), x, y, z, entity.level());
        this.setOwner(entity);
        this.ownerType = entity.getType();
    }

    public ThrowableBlock(Player player, BlockPos pos) {
        this(pos.getX(), pos.getY() + 1.5F, pos.getZ(), player);
    }

    protected ParticleOptions getTrailParticle() {
        return new DustParticleOptions(LAPIS_PARTICLE_COLOR, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        ParticleOptions particle = this.getTrailParticle();
        if (this.level().isClientSide) {
            for (int i = 0; i < 10; i++) {
                double x = this.getRandomX(1.0D);
                double y = this.getRandomY();
                double z = this.getRandomZ(1.0D);
                this.level().addParticle(particle, x, y, z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER_ENTITY_ID, OptionalInt.empty());
        builder.define(BLOCK_STATE, Blocks.GRASS_BLOCK.defaultBlockState());
    }

    @Override
    public void push(Entity entity) {
        if (entity != this.getOwner()) {
            super.push(entity);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 60; ++i) {
                double x = this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth();
                double y = this.getY() + 0.5 + (double) (this.random.nextFloat() * this.getBbHeight());
                double z = this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth();
                double xSpeed = (this.random.nextFloat() - this.random.nextFloat()) * 3.0F;
                double ySpeed = 0.5F + this.random.nextFloat() * 2.0F;
                double zSpeed = (this.random.nextFloat() - this.random.nextFloat()) * 3.0F;
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.getBlockState()), x, y, z, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    public void throwBlock(LivingEntity thrower) {
        this.setYRot(thrower.getYRot());
        this.setXRot(thrower.getXRot());
        float f = 0.4F;
        float g = this.getYRot() / 180.0F * Mth.PI;
        float h = this.getXRot() / 180.0F * Mth.PI;
        double x = -Mth.sin(g) * Mth.cos(h) * f;
        double y = -Mth.sin(h) * f;
        double z = Mth.cos(g) * Mth.cos(h) * f;
        this.shoot(x, y, z, 1.4F, 1.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        Entity thrower = this.getOwner();
        LivingEntity livingEntity = thrower instanceof LivingEntity ? (LivingEntity) thrower : null;
        boolean canOwnerPlace = livingEntity instanceof Player player && player.mayBuild();
        if (result instanceof BlockHitResult blockHitResult) {
            this.onHitBlock(blockHitResult);
            if (!this.level().isClientSide) {
                BlockPos blockPos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
                if (canOwnerPlace && this.level().getBlockState(blockPos).canBeReplaced() && this.getBlockState().canSurvive(this.level(), blockPos)) {
                    this.level().setBlockAndUpdate(blockPos, this.getBlockState());
                    SoundType soundType = this.getBlockState().getSoundType();
                    this.playSound(soundType.getPlaceSound(), (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                } else {
                    this.level().levelEvent(2001, blockPos, Block.getId(this.getBlockState()));
                }
            }
        }

        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(2.0), this::canHitEntity)) {
            if (livingEntity != null && !entity.is(livingEntity) && this.distanceToSqr(entity) <= 4.0) {
                Block block = this.getBlockState().getBlock();
                float strength = Math.max(block.defaultDestroyTime(), block.getExplosionResistance());
                float amount = 6.0F + this.random.nextInt(3) + strength;
                entity.hurt(this.level().damageSources().mobProjectile(this, livingEntity), amount);
            }
        }

        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    public BlockState getBlockState() {
        return this.entityData.get(BLOCK_STATE);
    }

    public void setBlockState(BlockState blockState) {
        this.entityData.set(BLOCK_STATE, blockState);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("BlockState", NbtUtils.writeBlockState(this.getBlockState()));
        if (this.ownerType != null) {
            compound.putString("OwnerType", BuiltInRegistries.ENTITY_TYPE.getKey(this.ownerType).toString());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("BlockState", 10)) {
            HolderLookup<Block> lookup = this.level().holderLookup(Registries.BLOCK);
            this.setBlockState(NbtUtils.readBlockState(lookup, compound.getCompound("BlockState")));
        }

        if (compound.contains("OwnerType")) {
            this.ownerType = EntityType.byString(compound.getString("OwnerType")).orElse(null);
        }
    }

}