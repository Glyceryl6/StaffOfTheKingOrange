package com.glyceryl6.kamikaze.enchantments;

import com.glyceryl6.kamikaze.registry.KEAttachmentTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.AttachmentType;

public record KamikazeElytraEffect(int placeholder) implements EnchantmentEntityEffect {

    public static final MapCodec<KamikazeElytraEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("placeholder").forGetter(KamikazeElytraEffect::placeholder)).apply(instance, KamikazeElytraEffect::new));

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        if (entity instanceof ServerPlayer player && !level.isClientSide()) {
            AttachmentType<Integer> type = KEAttachmentTypes.FALL_FLYING_TICKS.get();
            if ((player.isCrouching() || player.jumping) && !player.isFallFlying()) {
                player.setData(type, 0);
            }

            if (item.itemStack().canElytraFly(player)) {
                if (player.isFallFlying() && player.hasPose(Pose.FALL_FLYING) && !player.getAbilities().instabuild) {
                    player.setData(type, player.getData(type) + 1);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), 10, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                player.setData(type, 0);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }

}