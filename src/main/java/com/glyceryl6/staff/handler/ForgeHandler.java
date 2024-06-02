package com.glyceryl6.staff.handler;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.entities.Beeper;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.registry.ModKeyMappings;
import com.glyceryl6.staff.registry.ModMobEffects;
import com.glyceryl6.staff.registry.ModNetworks;
import com.glyceryl6.staff.server.commands.ModCommandCenter;
import com.glyceryl6.staff.server.network.RandomChangeStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.SetStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.StaffContinuousModeC2SPacket;
import com.glyceryl6.staff.utils.StaffConstantUtils;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Main.MOD_ID)
public class ForgeHandler {

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
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        DamageSource source = event.getSource();
        if (living instanceof Bee && source.is(DamageTypes.EXPLOSION)) {
            if (source.getEntity() instanceof Beeper) {
                event.setCanceled(true);
            }
        }

        if (living instanceof ServerPlayer player) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            if (itemInHand.getItem() instanceof StaffItem && !player.level().isClientSide) {
                BlockState state = StaffUniversalUtils.getCoreBlockState(itemInHand);
                boolean flag = state.getBlock() instanceof BeehiveBlock;
                if (source.getEntity() instanceof LivingEntity livingEntity && flag) {
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
                    UUID uuid = UUID.fromString("9586e5ab-157a-4658-ad80-b07552a9ca63");
                    ResolvableProfile newProfile = new ResolvableProfile(new GameProfile(uuid, "MHF_Herobrine"));
                    if (profile == null || profile.name().isPresent() && profile.name().get().equals("MHF_Steve")) {
                        String value = StaffConstantUtils.MHF_HEROBRINE_VALUE;
                        String signature = StaffConstantUtils.MHF_HEROBRINE_SIGNATURE;
                        Property property = new Property(("textures"), value, signature);
                        newProfile.properties().put("properties", property);
                        itemInHand.set(DataComponents.PROFILE, newProfile);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        Optional<Holder<MobEffect>> holder = ModMobEffects.STUN.getHolder();
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
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player != null) {
            if (ModKeyMappings.RANDOM_CHANGE_KEYBINDING.consumeClick()) {
                ModNetworks.sendToServer(new RandomChangeStaffBlockC2SPacket());
            }

            if (ModKeyMappings.ADD_REMOVE_KEYBINDING.consumeClick()) {
                ModNetworks.sendToServer(new SetStaffBlockC2SPacket());
            }

            if (ModKeyMappings.CONTINUOUS_MODE_KEYBINDING.consumeClick()) {
                ModNetworks.sendToServer(new StaffContinuousModeC2SPacket());
            }
        }
    }

}