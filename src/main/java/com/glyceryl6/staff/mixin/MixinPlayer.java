package com.glyceryl6.staff.mixin;

import com.glyceryl6.staff.api.IHasCobwebHookEntity;
import com.glyceryl6.staff.common.entities.CobwebHook;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements IHasCobwebHookEntity {

    public MixinPlayer(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "aiStep", at = @At(value = "TAIL"))
    public void aiStep(CallbackInfo ci) {
        CobwebHook cobwebHook = this.getCobwebHook();
        if (cobwebHook != null && cobwebHook.isInBlock()) {
            this.resetFallDistance();
            if (this.isControlledByLocalInstance()) {
                Vec3 vec3 = cobwebHook.position().subtract(this.getEyePosition());
                double g = cobwebHook.getLength();
                double d = vec3.length();
                if (d > g) {
                    double e = d / g * 0.1D;
                    this.addDeltaMovement(vec3.scale(1.0D / d).multiply(e, e * 1.1D, e));
                }
            }
        }
    }

}