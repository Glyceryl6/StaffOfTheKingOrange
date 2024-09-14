package com.glyceryl6.kamikaze.registry;

import com.glyceryl6.kamikaze.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class KEDamageTypes {

    public static final ResourceKey<DamageType> SUICIDE_EXPLOSION = ResourceKey.create(Registries.DAMAGE_TYPE, Main.prefix("suicide_explosion"));

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(SUICIDE_EXPLOSION, new DamageType("explosion.suicide", 0.1F));
    }

}