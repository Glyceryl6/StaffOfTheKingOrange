package com.glyceryl6.staff.mixin;

import com.glyceryl6.staff.api.IHasCobwebHookEntity;
import com.glyceryl6.staff.common.entities.CobwebHook;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements ILivingEntityExtension, IHasCobwebHookEntity {

    @Shadow public abstract ItemStack getItemInHand(InteractionHand hand);

    @Unique private CobwebHook KO$cobwebHook;

    public MixinLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "travel", at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
            target = "Lnet/minecraft/world/entity/LivingEntity;shouldDiscardFriction()Z"))
    public void travel(Vec3 travelVector, CallbackInfo ci, @Local(ordinal = 1) double d2, @Local(ordinal = 1) Vec3 vec35) {
        if (this.KO$cobwebHook != null && this.KO$cobwebHook.isInBlock() && !this.onGround()) {
            this.setDeltaMovement(vec35.x * 0.99D, d2 * 0.995D, vec35.z * 0.99D);
        }
    }

    @Override
    public CobwebHook KO$getCobwebHook() {
        return this.KO$cobwebHook;
    }

    @Override
    public void KO$setCobwebHook(CobwebHook cobwebHook) {
        this.KO$cobwebHook = cobwebHook;
    }

}