package com.glyceryl6.staff.api;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public interface IAbstractStaffFunction {

    default boolean enableUse() {
        return true;
    }

    default boolean enableUseTick() {
        return true;
    }

    default boolean enableUseOnBlock() {
        return true;
    }

    default boolean canPlaceBlock(UseOnContext context) {
        return false;
    }

    default void use(Level level, Player player, ItemStack stack) {
        this.useTick(level, player, stack);
    }

    default void useTick(Level level, Player player, ItemStack stack) {}

    default void useOnBlock(UseOnContext context) {}

    default void useOnEntity(Player player, InteractionHand hand, Entity target) {}

    default void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {}

    default void attackBlock(Level level, Player player, BlockPos pos) {}

    default void attackEntity(Level level, Player player, Entity target) {}

    default List<Block> placeableBlocks() {
        return new ArrayList<>();
    }

    default Multimap<Attribute, AttributeModifier> addAttributes(ItemStack stack) {
        return ImmutableMultimap.of();
    }

}