package com.glyceryl6.kamikaze.data;

import com.glyceryl6.kamikaze.Main;
import com.glyceryl6.kamikaze.registry.KEDamageTypes;
import com.glyceryl6.kamikaze.registry.KEEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, KEEnchantments::bootstrap)
            .add(Registries.DAMAGE_TYPE, KEDamageTypes::bootstrap);

    public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Main.MOD_ID));
    }

}