package com.glyceryl6.staff.handler;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.client.model.StaffModel;
import com.glyceryl6.staff.client.model.StalagmiteModel;
import com.glyceryl6.staff.client.model.geom.KOModelLayers;
import com.glyceryl6.staff.client.renderer.*;
import com.glyceryl6.staff.client.renderer.layers.SurroundingBlockLayer;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.registry.KOEntityTypes;
import com.glyceryl6.staff.registry.KOItems;
import com.glyceryl6.staff.registry.KOKeyMappings;
import com.glyceryl6.staff.server.network.*;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Objects;

@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(KOEntityTypes.STAFF_TNT.get(), TntRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.STAFF_WITHER_SKULL.get(), WitherSkullRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.STAFF_FIREBALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.THROWN_BOOK_AND_PAPER.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.BONE_MEAL.get(), EmptyRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.ENCHANT.get(), EmptyRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.ICE_BOMB.get(), EmptyRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.SIGNAL.get(), EmptyRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.SMELTING.get(), EmptyRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.MUSICAL_NOTE.get(), MusicalNoteRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.THROWABLE_BLOCK.get(), ThrowableBlockRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.PLACED_STAFF.get(), PlacedStaffRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.STALAGMITE.get(), StalagmiteRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.BEEPER.get(), BeeperRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.HEROBRINE_HEAD.get(), SkullRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.COBWEB_HOOK.get(), CobwebHookRenderer::new);
        event.registerEntityRenderer(KOEntityTypes.COBWEB.get(), context ->
                new ThrownItemRenderer<>(context, 2.0F, Boolean.FALSE));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(KOModelLayers.STAFF_LAYER, StaffModel::createBodyLayer);
        event.registerLayerDefinition(KOModelLayers.BEEPER_LAYER, BeeModel::createBodyLayer);
        event.registerLayerDefinition(KOModelLayers.PLAYER_HEAD_LAYER, SkullModel::createHumanoidHeadLayer);
        event.registerLayerDefinition(KOModelLayers.STALAGMITE_LAYER, StalagmiteModel::createBodyLayer);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerRenderLayers(EntityRenderersEvent.AddLayers event) {
        for (EntityType<?> type : event.getEntityTypes()) {
            EntityRenderer<?> renderer = event.getRenderer(type);
            if (renderer instanceof LivingEntityRenderer<?, ?> living) {
                registerRenderLayers(living);
            }
        }

        event.getSkins().forEach(renderer -> {
            LivingEntityRenderer<Player, EntityModel<Player>> skin = event.getSkin(renderer);
            registerRenderLayers(Objects.requireNonNull(skin));
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static <T extends LivingEntity, M extends EntityModel<T>> void registerRenderLayers(LivingEntityRenderer<T, M> renderer) {
        renderer.addLayer(new SurroundingBlockLayer<>(renderer));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new StaffItem.CustomRenderer(), KOItems.STAFF.get());
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(KOEntityTypes.BEEPER.get(), Bee.createAttributes().build());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KOKeyMappings.ADD_REMOVE_KEYBINDING);
        event.register(KOKeyMappings.RANDOM_CHANGE_KEYBINDING);
        event.register(KOKeyMappings.CONTINUOUS_MODE_KEYBINDING);
    }

    @SubscribeEvent
    public static void registerNetworks(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(RandomChangeStaffBlockC2SPacket.TYPE,
                RandomChangeStaffBlockC2SPacket.STREAM_CODEC,
                RandomChangeStaffBlockC2SPacket::handle);
        registrar.playToServer(SetStaffBlockC2SPacket.TYPE,
                SetStaffBlockC2SPacket.STREAM_CODEC,
                SetStaffBlockC2SPacket::handle);
        registrar.playToClient(SetEntityGlintS2CPacket.TYPE,
                SetEntityGlintS2CPacket.STREAM_CODEC,
                SetEntityGlintS2CPacket::handle);
        registrar.playToServer(SetStaffCommandC2SPacket.TYPE,
                SetStaffCommandC2SPacket.STREAM_CODEC,
                SetStaffCommandC2SPacket::handle);
        registrar.playToServer(ShowStaffSurroundingBlockS2CPacket.TYPE,
                ShowStaffSurroundingBlockS2CPacket.STREAM_CODEC,
                ShowStaffSurroundingBlockS2CPacket::handle);
        registrar.playToServer(StaffContinuousModeC2SPacket.TYPE,
                StaffContinuousModeC2SPacket.STREAM_CODEC,
                StaffContinuousModeC2SPacket::serverSideHandle);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerItemProperties(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(KOItems.STAFF.get(), Main.prefix("using"), ((stack, level, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F)));
    }

}