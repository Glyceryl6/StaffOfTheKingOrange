package com.glyceryl6.staff;

import com.glyceryl6.staff.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import java.util.Locale;

@Mod(Main.MOD_ID)
public class Main {

    public static final String MOD_ID = "staff_of_the_king_orange";

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        KOCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        KOBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        KODataComponents.DATA_COMPONENT_TYPE.register(modEventBus);
        KOParticleTypes.PARTICLE_TYPES.register(modEventBus);
        KOEntityTypes.ENTITY_TYPES.register(modEventBus);
        KOMobEffects.MOB_EFFECTS.register(modEventBus);
        KOBlocks.BLOCKS.register(modEventBus);
        KOItems.ITEMS.register(modEventBus);
    }

    public static ResourceLocation prefix(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name.toLowerCase(Locale.ROOT));
    }

}