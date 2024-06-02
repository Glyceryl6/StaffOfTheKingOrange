package com.glyceryl6.staff.functions.other;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.registry.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

public class StaffWithBell implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        BlockPos pos = player.blockPosition();
        AABB aabb = new AABB(pos).inflate(48.0);
        level.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0F, 1.0F);
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, aabb);
        if (!level.isClientSide) {
            for (LivingEntity entity : nearbyEntities) {
                if (entity.isAlive() && !entity.isRemoved() && pos.closerToCenterThan(entity.position(), 32.0)) {
                    entity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, level.getGameTime());
                }
            }
        }
    }

    @Override
    public void attackEntity(Level level, Player player, Entity target) {
        Optional<Holder<MobEffect>> holder = ModMobEffects.STUN.getHolder();
        target.playSound(SoundEvents.BELL_BLOCK, 2.0F, 1.0F);
        if (!level.isClientSide && target instanceof LivingEntity entity && holder.isPresent()) {
            entity.addEffect(new MobEffectInstance(holder.get(), 200));
        }
    }

}