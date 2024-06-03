package com.glyceryl6.staff.common.entities.projectile.visible;

import com.glyceryl6.staff.utils.StaffUniversalUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;

public class AbstractPlayerHead extends AbstractHurtingProjectile {

    private static final EntityDataAccessor<String> PLAYER_NAME = SynchedEntityData.defineId(AbstractPlayerHead.class, EntityDataSerializers.STRING);

    public AbstractPlayerHead(EntityType<? extends AbstractPlayerHead> type, Level level) {
        super(type, level);
    }

    public AbstractPlayerHead(EntityType<? extends AbstractPlayerHead> type, Level level, LivingEntity shooter, double offsetX, double offsetY, double offsetZ) {
        super(type, shooter, offsetX, offsetY, offsetZ, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(PLAYER_NAME, "");
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected float getLiquidInertia() {
        return this.getInertia();
    }

    public ResolvableProfile getProfile() {
        return StaffUniversalUtils.getPlayerProfile(this.level(), this.entityData.get(PLAYER_NAME));
    }

    public void setPlayerName(String playerName) {
        this.entityData.set(PLAYER_NAME, playerName);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("name", this.entityData.get(PLAYER_NAME));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setPlayerName(compound.getString("name"));
    }

}