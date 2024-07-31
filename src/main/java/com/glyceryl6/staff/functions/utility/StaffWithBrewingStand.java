package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class StaffWithBrewingStand implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        ThrownPotion thrownPotion = new ThrownPotion(level, player);
        level.playSound(null, player.getOnPos(), SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
        thrownPotion.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
        thrownPotion.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
        Item potionItem = level.random.nextBoolean() ? Items.SPLASH_POTION : Items.LINGERING_POTION;
        ItemStack potionStack = PotionContents.createItemStack(potionItem, getRandomPotion());
        thrownPotion.setItem(potionStack);
        level.addFreshEntity(thrownPotion);
    }

    private static Holder<Potion> getRandomPotion() {
        RandomSource random = RandomSource.create();
        List<Holder<Potion>> list = new ArrayList<>();
        BuiltInRegistries.POTION.holders().forEach(list::add);
        return list.get(random.nextInt(list.size()));
    }

}