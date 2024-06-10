package com.glyceryl6.staff.common.entities;

import com.glyceryl6.staff.api.IHasCobwebHookEntity;
import com.glyceryl6.staff.registry.ModEntityTypes;
import com.glyceryl6.staff.registry.ModItems;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CobwebHook extends Projectile {

    public static final EntityDataAccessor<Boolean> IN_BLOCK = SynchedEntityData.defineId(CobwebHook.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Float> LENGTH = SynchedEntityData.defineId(CobwebHook.class, EntityDataSerializers.FLOAT);

    public CobwebHook(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    public CobwebHook(Level level, Player player) {
        this(ModEntityTypes.COBWEB_HOOK.get(), level);
        this.setOwner(player);
        this.setPos(player.getX(), player.getEyeY() - 0.1D, player.getZ());
        this.setDeltaMovement(player.getViewVector(1.0F).scale(5.0F));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IN_BLOCK, false);
        this.entityData.define(LENGTH, 0.0F);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean teleport) {}

    @Override
    public void tick() {
        super.tick();
        Player player = this.getPlayer();
        if (player != null && (this.level().isClientSide || !this.discardIfInvalid(player))) {
            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onHit(hitResult);
            }

            this.setPos(hitResult.getLocation());
            this.checkInsideBlocks();
        } else {
            this.discard();
        }
    }

    private boolean isPlayerHoldingCobwebStaff(Player player) {
        return player.isHolding(stack -> {
            BlockState state = StaffUniversalUtils.getCoreBlockState(stack);
            return stack.is(ModItems.STAFF.get()) && state.is(Blocks.COBWEB);
        });
    }

    private boolean discardIfInvalid(Player player) {
        if (!player.isRemoved() && player.isAlive() && this.isPlayerHoldingCobwebStaff(player)) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return false;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.setDeltaMovement(Vec3.ZERO);
        this.setInBlock(true);
        Player player = this.getPlayer();
        if (player != null) {
            double d = player.getEyePosition().subtract(result.getLocation()).length();
            this.setLength(Math.max((float)d * 0.5F - 3.0F, 1.5F));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("InBlock", this.isInBlock());
        compound.putFloat("Length", this.getLength());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setInBlock(compound.getBoolean("InBlock"));
        this.setLength(compound.getFloat("Length"));
    }

    private void setInBlock(boolean inBlock) {
        this.entityData.set(IN_BLOCK, inBlock);
    }

    private void setLength(float length) {
        this.entityData.set(LENGTH, length);
    }

    public boolean isInBlock() {
        return this.entityData.get(IN_BLOCK);
    }

    public float getLength() {
        return this.entityData.get(LENGTH);
    }

    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public void remove(RemovalReason reason) {
        this.setHookForPlayer(null);
        super.remove(reason);
    }

    @Override
    public void onClientRemoval() {
        this.setHookForPlayer(null);
    }

    @Override
    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        this.setHookForPlayer(this);
    }

    private void setHookForPlayer(@Nullable CobwebHook cobwebHook) {
        if (this.getPlayer() instanceof IHasCobwebHookEntity entity) {
            entity.setCobwebHook(cobwebHook);
        }
    }

    @Nullable
    public Player getPlayer() {
        return this.getOwner() instanceof Player player ? player : null;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new ClientboundAddEntityPacket(this, entity == null ? this.getId() : entity.getId());
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        if (this.getPlayer() == null) {
            this.kill();
        }
    }

}