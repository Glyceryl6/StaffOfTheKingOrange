package com.glyceryl6.staff.handler;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.registry.ModEntityTypes;
import com.glyceryl6.staff.registry.ModItems;
import com.glyceryl6.staff.registry.ModNetworks;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModHandler {

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.BEEPER.get(), Bee.createAttributes().build());
    }

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.STAFF);
        }
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ModNetworks.register();
    }

}