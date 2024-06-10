package com.glyceryl6.staff.mixin;

import com.glyceryl6.staff.api.IHasCobwebHookEntity;
import com.glyceryl6.staff.api.IHasEnchantmentGlintEntity;
import com.glyceryl6.staff.common.entities.CobwebHook;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IForgeLivingEntity, IHasCobwebHookEntity, IHasEnchantmentGlintEntity {

    private static final EntityDataAccessor<Boolean> IS_GLINT = SynchedEntityData.defineId(MixinLivingEntity.class, EntityDataSerializers.BOOLEAN);
    private CobwebHook cobwebHook;

    public MixinLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    protected void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(IS_GLINT, Boolean.FALSE);
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "TAIL"))
    public void addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("IsGlint", this.isGlint());
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "TAIL"))
    public void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.setGlint(compound.getBoolean("IsGlint"));
    }

    @Inject(method = "travel", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
            target = "Lnet/minecraft/world/entity/LivingEntity;shouldDiscardFriction()Z"))
    public void travel(Vec3 pTravelVector, CallbackInfo ci, double d0, AttributeInstance gravity, boolean flag, FluidState fluidstate, BlockPos blockpos, float f2, float f3, Vec3 vec35, double d2) {
        if (this.cobwebHook != null && this.cobwebHook.isInBlock() && !this.onGround()) {
            this.setDeltaMovement(vec35.x * 0.99D, d2 * 0.995D, vec35.z * 0.99D);
        }
    }

    @Override
    public boolean isGlint() {
        return this.entityData.get(IS_GLINT);
    }

    @Override
    public void setGlint(boolean glint) {
        this.entityData.set(IS_GLINT, glint);
    }

    @Override
    public CobwebHook getCobwebHook() {
        return this.cobwebHook;
    }

    @Override
    public void setCobwebHook(CobwebHook cobwebHook) {
        this.cobwebHook = cobwebHook;
    }

}