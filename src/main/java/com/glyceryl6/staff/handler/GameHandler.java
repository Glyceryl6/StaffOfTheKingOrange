package com.glyceryl6.staff.handler;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.registry.KOKeyMappings;
import com.glyceryl6.staff.registry.KOMobEffects;
import com.glyceryl6.staff.server.commands.ModCommandCenter;
import com.glyceryl6.staff.server.network.RandomChangeStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.SetStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.StaffContinuousModeC2SPacket;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

@EventBusSubscriber(modid = Main.MOD_ID)
public class GameHandler {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        new ModCommandCenter(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack itemStack = event.getItemStack();
        Level level = event.getLevel();
        if (itemStack.getItem() instanceof StaffItem && !level.isClientSide) {
            StaffUniversalUtils.getStaffFunction(itemStack).attackBlock(level, event.getEntity(), event.getPos());
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
        if (itemInHand.getItem() instanceof StaffItem && !level.isClientSide) {
            StaffUniversalUtils.getStaffFunction(itemInHand).attackEntity(level, player, event.getTarget());
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            if (itemInHand.getItem() instanceof StaffItem && !player.level().isClientSide) {
                BlockState state = StaffUniversalUtils.getCoreBlockState(itemInHand);
                boolean flag = state.getBlock() instanceof BeehiveBlock;
                if (event.getSource().getEntity() instanceof LivingEntity livingEntity && flag) {
                    AABB aabb = AABB.unitCubeFromLowerCorner(player.position()).inflate(64.0D, 10.0D, 64.0D);
                    player.level().getEntitiesOfClass(Bee.class, aabb).forEach(entity -> entity.setTarget(livingEntity));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            if (itemInHand.getItem() instanceof StaffItem && !player.level().isClientSide) {
                ResolvableProfile profile = itemInHand.get(DataComponents.PROFILE);
                if (StaffUniversalUtils.getCoreBlockState(itemInHand).getBlock() instanceof PlayerHeadBlock) {
                    if (profile == null || profile.name().isPresent() && profile.name().get().equals("MHF_Steve")) {
                        StaffUniversalUtils.setPlayerHeadForStaff(player.level(), itemInHand, "MHF_Herobrine");
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        Optional<Holder.Reference<MobEffect>> holder = BuiltInRegistries.MOB_EFFECT.getHolder(KOMobEffects.STUN.getId());
        if (holder.isPresent() && event.getEntity().hasEffect(holder.get())) {
            Input input = event.getInput();
            input.up = false;
            input.down = false;
            input.left = false;
            input.right = false;
            input.forwardImpulse = 0.0F;
            input.leftImpulse = 0.0F;
            input.jumping = false;
            input.shiftKeyDown = false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        if (Minecraft.getInstance().player != null) {
            if (KOKeyMappings.RANDOM_CHANGE_KEYBINDING.consumeClick()) {
                PacketDistributor.sendToServer(new RandomChangeStaffBlockC2SPacket(0));
            }

            if (KOKeyMappings.ADD_REMOVE_KEYBINDING.consumeClick()) {
                PacketDistributor.sendToServer(new SetStaffBlockC2SPacket(0));
            }

            if (KOKeyMappings.CONTINUOUS_MODE_KEYBINDING.consumeClick()) {
                PacketDistributor.sendToServer(new StaffContinuousModeC2SPacket(0));
            }
        }
    }

}