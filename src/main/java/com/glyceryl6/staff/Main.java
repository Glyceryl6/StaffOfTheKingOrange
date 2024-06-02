package com.glyceryl6.staff;

import com.glyceryl6.staff.registry.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Locale;

@Mod(Main.MOD_ID)
public class Main {

    public static final String MOD_ID = "staff_of_the_king_orange";
    public static final ModelLayerLocation STAFF_LAYER = new ModelLayerLocation(prefix("staff"), "main");
    public static final ModelLayerLocation BEEPER_LAYER = new ModelLayerLocation(prefix("beeper"), "main");
    public static final ModelLayerLocation PLAYER_HEAD_LAYER = new ModelLayerLocation(prefix("player_head"), "main");

    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModMobEffects.MOB_EFFECTS.register(modEventBus);
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModDataComponents.DATA_COMPONENT_TYPE.register(modEventBus);
        ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }

}