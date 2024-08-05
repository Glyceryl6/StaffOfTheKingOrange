package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class KODamageTypes {

    public static final ResourceKey<DamageType> REDSTONE_BEAM = createKey("redstone_beam");
    public static final ResourceKey<DamageType> KNOWLEDGE = createKey("knowledge");
    public static final ResourceKey<DamageType> ANVIL_THUMP = createKey("anvil_thump");
    public static final ResourceKey<DamageType> NOISE = createKey("noise");

    private static ResourceKey<DamageType> createKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, Main.prefix(name));
    }

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(REDSTONE_BEAM, new DamageType("redstoneBeam", 0.1F));
        context.register(KNOWLEDGE, new DamageType("knowledge", 0.1F));
        context.register(ANVIL_THUMP, new DamageType("anvilThump", 0.1F));
        context.register(NOISE, new DamageType("noise", 0.0F));
    }

}