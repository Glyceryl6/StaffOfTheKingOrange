package com.glyceryl6.staff.handler;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.client.model.StaffModel;
import com.glyceryl6.staff.client.renderer.*;
import com.glyceryl6.staff.registry.ModEntityTypes;
import com.glyceryl6.staff.registry.ModItems;
import com.glyceryl6.staff.registry.ModKeyMappings;
import com.glyceryl6.staff.registry.ModNetworks;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.WitherSkullRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.STAFF_TNT.get(), TntRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.STAFF_WITHER_SKULL.get(), WitherSkullRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.STAFF_FIREBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.THROWN_ITEM.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BONE_MEAL.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ENCHANT.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ICE_BOMB.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SIGNAL.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SMELTING.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.MUSICAL_NOTE.get(), MusicalNoteRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.FAKE_BLOCK.get(), FakeBlockRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.PLACED_STAFF.get(), PlacedStaffRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BEEPER.get(), BeeperRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.HEROBRINE_HEAD.get(), SkullRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.COBWEB_HOOK.get(), CobwebHookRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.COBWEB.get(), context ->
                new ThrownItemRenderer<>(context, 2.0F, Boolean.FALSE));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(Main.STAFF_LAYER, StaffModel::createBodyLayer);
        event.registerLayerDefinition(Main.BEEPER_LAYER, BeeModel::createBodyLayer);
        event.registerLayerDefinition(Main.PLAYER_HEAD_LAYER, SkullModel::createHumanoidHeadLayer);
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.BEEPER.get(), Bee.createAttributes().build());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.ADD_REMOVE_KEYBINDING);
        event.register(ModKeyMappings.RANDOM_CHANGE_KEYBINDING);
        event.register(ModKeyMappings.CONTINUOUS_MODE_KEYBINDING);
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerItemProperties(FMLClientSetupEvent event) {
        ItemProperties.register(ModItems.STAFF.get(), Main.prefix("using"), ((stack, level, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));
    }

}