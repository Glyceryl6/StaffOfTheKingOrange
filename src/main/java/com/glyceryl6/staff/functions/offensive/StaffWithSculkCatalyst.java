package com.glyceryl6.staff.functions.offensive;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class StaffWithSculkCatalyst implements INormalStaffFunction {

    @Override
    public boolean enableUseOnBlock() {
        return false;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        int t = stack.getUseDuration() - timeCharged;
        if (t > 10 && level instanceof ServerLevel serverLevel && !serverLevel.isClientSide) {
            AABB aabb = new AABB(entity.blockPosition()).inflate(30.0D);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb);
            if (!entities.isEmpty()) {
                entities.stream().filter(target -> entity.closerThan(target, (15.0D), (20.0D))).forEach(target -> {
                    Vec3 vec3 = entity.position().add(entity.getAttachments().get(
                            EntityAttachment.WARDEN_CHEST, 0, entity.getYRot()));
                    Vec3 vec31 = target.getEyePosition().subtract(vec3);
                    Vec3 vec32 = vec31.normalize();
                    for (int j = 1; j < Mth.floor(vec31.length()) + 7; j++) {
                        Vec3 vec33 = vec3.add(vec32.scale(j));
                        serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z,
                                1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }

                    entity.playSound(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
                    if (target.hurt(serverLevel.damageSources().sonicBoom(entity), (float) t)) {
                        double d1 = 0.5D * (1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        double d0 = 2.5D * (1.0D - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                        target.push(vec32.x() * d0, vec32.y() * d1, vec32.z() * d0);
                    }
                });
            }
        }
    }

}