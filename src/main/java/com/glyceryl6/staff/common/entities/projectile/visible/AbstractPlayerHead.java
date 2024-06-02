package com.glyceryl6.staff.common.entities.projectile.visible;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class AbstractPlayerHead extends AbstractHurtingProjectile {

    private static final EntityDataAccessor<String> ID = SynchedEntityData.defineId(AbstractPlayerHead.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> NAME = SynchedEntityData.defineId(AbstractPlayerHead.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> VALUE = SynchedEntityData.defineId(AbstractPlayerHead.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> SIGNATURE = SynchedEntityData.defineId(AbstractPlayerHead.class, EntityDataSerializers.STRING);

    public AbstractPlayerHead(EntityType<? extends AbstractPlayerHead> type, Level level) {
        super(type, level);
    }

    public AbstractPlayerHead(EntityType<? extends AbstractPlayerHead> type, Level level, LivingEntity shooter, double offsetX, double offsetY, double offsetZ) {
        super(type, shooter, offsetX, offsetY, offsetZ, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ID, "");
        builder.define(NAME, "");
        builder.define(VALUE, "");
        builder.define(SIGNATURE, "");
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
        UUID uuid = UUID.fromString(this.getProfiles()[0]);
        ResolvableProfile newProfile = new ResolvableProfile(new GameProfile(uuid, this.getProfiles()[1]));
        Property property = new Property("textures", this.getProfiles()[2], this.getProfiles()[3]);
        newProfile.properties().put("properties", property);
        return newProfile;
    }

    private String[] getProfiles() {
        String uuid = this.entityData.get(ID);
        String name = this.entityData.get(NAME);
        String value = this.entityData.get(VALUE);
        String signature = this.entityData.get(SIGNATURE);
        return new String[] {uuid, name, value, signature};
    }

    public void setProfiles(String uuid, String name, String value, String signature) {
        this.entityData.set(ID, uuid);
        this.entityData.set(NAME, name);
        this.entityData.set(VALUE, value);
        this.entityData.set(SIGNATURE, signature);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("id", this.getProfiles()[0]);
        compound.putString("name", this.getProfiles()[1]);
        compound.putString("value", this.getProfiles()[2]);
        compound.putString("signature", this.getProfiles()[3]);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setProfiles(compound.getString("id"), compound.getString("name"),
                compound.getString("value"), compound.getString("signature"));
    }

}