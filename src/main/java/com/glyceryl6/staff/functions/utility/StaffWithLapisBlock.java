package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.common.entities.projectile.visible.ThrowableBlock;
import com.glyceryl6.staff.registry.KODataComponents;
import com.glyceryl6.staff.utils.StaffSpecialUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class StaffWithLapisBlock implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        BundleContents bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents != null && !bundleContents.isEmpty()) {
            List<Integer> availableIndexes = this.containedBlockIndexes(bundleContents);
            if (!availableIndexes.isEmpty()) {
                RandomSource random = level.random;
                random.setSeed(stack.getOrDefault(KODataComponents.RANDOM_SEED, random.nextLong()));
                int randomIndex = random.nextInt(availableIndexes.size());
                Item item = bundleContents.getItemUnsafe(randomIndex).getItem();
                if (item instanceof BlockItem blockItem) {
                    ThrowableBlock throwableBlock = new ThrowableBlock(player, player.blockPosition());
                    throwableBlock.setBlockState(blockItem.getBlock().defaultBlockState());
                    throwableBlock.throwBlock(player);
                    level.addFreshEntity(throwableBlock);
                    this.removeItem(stack, randomIndex);
                } else {
                    availableIndexes.remove(randomIndex);
                }
            }
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack itemInHand = context.getItemInHand();
        DataComponentType<BundleContents> type = DataComponents.BUNDLE_CONTENTS;
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundSource.BLOCKS, (1.0F), (level.random.nextFloat() * 0.1F + 0.9F));
        BundleContents bundleContents = itemInHand.get(type);
        if (!level.isClientSide && bundleContents != null) {
            BundleContents.Mutable mutable = new BundleContents.Mutable(bundleContents);
            ItemStack itemStack = new ItemStack(level.getBlockState(pos).getBlock());
            if (itemStack.getItem() instanceof BlockItem) {
                this.tryInsert(mutable.items, itemStack);
                itemInHand.set(type, mutable.toImmutable());
                level.removeBlock(pos, Boolean.FALSE);
            }
        }
    }

    @Override
    public void useOnEntity(Player player, InteractionHand hand, Entity target) {
        StaffSpecialUtils.setEntityGlint(player.level(), target);
    }

    private List<Integer> containedBlockIndexes(BundleContents bundleContents) {
        List<Integer> availableIndexes = new ArrayList<>();
        for (int i = 0; i < bundleContents.size(); i++) {
            if (bundleContents.getItemUnsafe(i).getItem() instanceof BlockItem) {
                availableIndexes.add(i);
            }
        }

        return availableIndexes;
    }

    private void removeItem(ItemStack bundleItemStack, int index) {
        BundleContents bundleContents = bundleItemStack.get(DataComponents.BUNDLE_CONTENTS);
        if (bundleContents != null && !bundleContents.isEmpty()) {
            List<ItemStack> stacks = new ArrayList<>(bundleContents.itemCopyStream().toList());
            ItemStack itemStack = bundleContents.getItemUnsafe(index).copy();
            itemStack.shrink(1);
            if (!itemStack.isEmpty()) {
                stacks.set(index, itemStack);
            } else {
                stacks.remove(index);
            }

            bundleContents = new BundleContents(stacks);
            bundleItemStack.set(DataComponents.BUNDLE_CONTENTS, bundleContents);
        }
    }

    private int findStackIndex(List<ItemStack> items, ItemStack stack) {
        for (int i = 0; i < items.size(); i++) {
            boolean isCompleteSame = ItemStack.isSameItemSameComponents(items.get(i), stack);
            boolean isLessThanMaxStackSize = items.get(i).getCount() < 99;
            if (isCompleteSame && isLessThanMaxStackSize) {
                return i;
            }
        }

        return -1;
    }

    private void tryInsert(List<ItemStack> items, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().canFitInsideContainerItems()) {
            int i = stack.getCount();
            int j = this.findStackIndex(items, stack);
            if (j != -1) {
                ItemStack stack1 = items.remove(j);
                ItemStack stack2 = stack1.copyWithCount(stack1.getCount() + i);
                stack.shrink(i);
                items.addFirst(stack2);
            } else {
                items.addFirst(stack.split(i));
            }
        }
    }

}