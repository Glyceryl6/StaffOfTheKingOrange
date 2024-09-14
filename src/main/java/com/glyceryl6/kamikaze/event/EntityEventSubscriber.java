package com.glyceryl6.kamikaze.event;

import com.glyceryl6.kamikaze.Main;
import com.glyceryl6.kamikaze.network.CompletelyInvisibleS2CPacket;
import com.glyceryl6.kamikaze.registry.KEAttachmentTypes;
import com.glyceryl6.kamikaze.registry.KEDamageTypes;
import com.glyceryl6.kamikaze.registry.KEEnchantments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Main.MOD_ID)
public class EntityEventSubscriber {

    @SubscribeEvent
    public static void onLivingAttack(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            float amount = event.getAmount();
            Level level = player.level();
            DamageSource source = event.getSource();
            ItemStack itemInChest = player.getItemBySlot(EquipmentSlot.CHEST);
            AttachmentType<Integer> type = KEAttachmentTypes.FALL_FLYING_TICKS.get();
            int i = itemInChest.getEnchantmentLevel(KEEnchantments.get(level, KEEnchantments.KAMIKAZE_ELYTRA));
            if (itemInChest.canElytraFly(player) && i > 0 && !level.isClientSide) {
                if (source.is(DamageTypeTags.IS_FALL) && player.getData(type) > 0 || source.is(DamageTypes.FLY_INTO_WALL)) {
                    player.stopFallFlying();
                    player.setData(KEAttachmentTypes.IS_EXPLODER, true);
                    itemInChest.hurtAndBreak(Mth.floor(amount), player, EquipmentSlot.CHEST);
                    DamageSource explosion = player.damageSources().source(KEDamageTypes.SUICIDE_EXPLOSION);
                    level.explode((null), explosion, (null), player.position(), amount, Boolean.FALSE, Level.ExplosionInteraction.MOB);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (event.getSource().is(KEDamageTypes.SUICIDE_EXPLOSION)) {
            entity.setNoGravity(true);
            if (entity instanceof ServerPlayer serverPlayer) {
                CompletelyInvisibleS2CPacket packet = new CompletelyInvisibleS2CPacket(Boolean.TRUE);
                PacketDistributor.sendToPlayer(serverPlayer, packet);
            }
        }
    }

}