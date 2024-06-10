package com.glyceryl6.staff.handler;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.server.commands.ModCommandCenter;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
                GameProfile profile = NbtUtils.readGameProfile(itemInHand.getOrCreateTag().getCompound("SkullOwner"));
                if (StaffUniversalUtils.getCoreBlockState(itemInHand).getBlock() instanceof PlayerHeadBlock) {
                    if (profile == null || profile.getName().equals("MHF_Steve")) {
                        StaffUniversalUtils.setPlayerHeadForStaff(itemInHand, "MHF_Herobrine");
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

}