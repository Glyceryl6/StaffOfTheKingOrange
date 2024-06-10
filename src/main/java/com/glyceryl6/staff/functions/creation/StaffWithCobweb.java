package com.glyceryl6.staff.functions.creation;

import com.glyceryl6.staff.api.IHasCobwebHookEntity;
import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.common.entities.CobwebHook;
import com.glyceryl6.staff.common.entities.projectile.visible.Cobweb;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class StaffWithCobweb implements INormalStaffFunction {

    private boolean isEffective(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("IsEffective");
    }

    @Override
    public boolean canPlaceBlock(UseOnContext context) {
        boolean flag = this.isEffective(context.getItemInHand());
        if (!context.getLevel().isClientSide && flag) {
            if (context.getPlayer() instanceof IHasCobwebHookEntity entity) {
                return entity.getCobwebHook() == null;
            }
        }

        return false;
    }

    @Override
    public void use(Level level, Player player, ItemStack stack) {
        if (player instanceof IHasCobwebHookEntity entity && this.isEffective(stack)) {
            CobwebHook cobwebHook = entity.getCobwebHook();
            if (cobwebHook != null) {
                if (!level.isClientSide) {
                    cobwebHook.discard();
                    entity.setCobwebHook(null);
                }

                this.playSound(level, player, SoundEvents.FISHING_BOBBER_RETRIEVE);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            } else {
                if (!level.isClientSide) {
                    level.addFreshEntity(new CobwebHook(level, player));
                }

                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                this.playSound(level, player, SoundEvents.FISHING_BOBBER_THROW);
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
            }
        }
    }

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide && this.isEffective(stack)) {
            Cobweb cobweb = new Cobweb(player, 0.0D, 0.0D, 0.0D, level);
            cobweb.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            cobweb.setDeltaMovement(player.getViewVector(1.0F).scale(2.5F));
            cobweb.setItem(Items.COBWEB.getDefaultInstance());
            level.addFreshEntity(cobweb);
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        boolean flag = this.isEffective(context.getItemInHand());
        if (player instanceof IHasCobwebHookEntity entity && flag) {
            CobwebHook cobwebHook = entity.getCobwebHook();
            if (cobwebHook != null) {
                if (!level.isClientSide) {
                    cobwebHook.discard();
                    entity.setCobwebHook(null);
                }

                this.playSound(level, player, SoundEvents.FISHING_BOBBER_RETRIEVE);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }
        }
    }

    private void playSound(Level level, Player player, SoundEvent soundEvent) {
        float pitch = 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.NEUTRAL, 1.0F, pitch);
    }

}