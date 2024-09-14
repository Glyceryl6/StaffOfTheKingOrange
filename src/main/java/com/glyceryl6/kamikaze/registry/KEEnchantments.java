package com.glyceryl6.kamikaze.registry;

import com.glyceryl6.kamikaze.Main;
import com.glyceryl6.kamikaze.enchantments.KamikazeElytraEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.level.Level;

public class KEEnchantments {

    public static final ResourceKey<Enchantment> KAMIKAZE_ELYTRA = createKey("kamikaze_elytra");

    private static ResourceKey<Enchantment> createKey(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, Main.prefix(name));
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> itemLookup = context.lookup(Registries.ITEM);
        register(context, KAMIKAZE_ELYTRA, Enchantment.enchantment(Enchantment.definition(
               itemLookup.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                        2, 1, Enchantment.constantCost(40),
                        Enchantment.constantCost(70), 4, EquipmentSlotGroup.CHEST))
                .withEffect(EnchantmentEffectComponents.TICK, new KamikazeElytraEffect(0)));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    public static Holder<Enchantment> get(Level level, ResourceKey<Enchantment> key) {
        RegistryAccess registryAccess = level.registryAccess();
        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }

}