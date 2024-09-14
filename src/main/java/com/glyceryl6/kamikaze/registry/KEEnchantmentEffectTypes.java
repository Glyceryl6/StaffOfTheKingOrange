package com.glyceryl6.kamikaze.registry;

import com.glyceryl6.kamikaze.Main;
import com.glyceryl6.kamikaze.enchantments.KamikazeElytraEffect;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class KEEnchantmentEffectTypes {

    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENCHANTMENT_ENTITY_EFFECT_TYPES =
            DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Main.MOD_ID);

    static {
        ENCHANTMENT_ENTITY_EFFECT_TYPES.register("kamikaze_elytra", () -> KamikazeElytraEffect.CODEC);
    }

}