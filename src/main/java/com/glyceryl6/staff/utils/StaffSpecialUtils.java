package com.glyceryl6.staff.utils;

import com.glyceryl6.staff.api.IHasEnchantmentGlintEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StaffSpecialUtils {

    public static void smeltingItemOrBlock(RecipeType<? extends AbstractCookingRecipe> recipeType, BlockPos pos, Level level) {
        ItemStack blockStack = new ItemStack(level.getBlockState(pos).getBlock());
        Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> holder1 =
                level.getRecipeManager().getRecipeFor(recipeType, new SimpleContainer(blockStack), level);
        if (holder1.isPresent()) {
            ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), blockStack);
            itemEntity.setNeverPickUp();
            level.removeBlock(pos, Boolean.FALSE);
            level.addFreshEntity(itemEntity);
        }

        List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(0.6D));
        if (!itemEntities.isEmpty()) {
            itemEntities.forEach(entity -> {
                ItemStack stack = entity.getItem();
                Optional<? extends RecipeHolder<? extends AbstractCookingRecipe>> holder2 =
                        level.getRecipeManager().getRecipeFor(recipeType, new SimpleContainer(stack), level);
                if (holder2.isPresent()) {
                    ItemStack resultItem = holder2.get().value().getResultItem(level.registryAccess());
                    resultItem.setCount(stack.getCount());
                    entity.setItem(resultItem);
                    entity.setNoPickUpDelay();
                }
            });
        }
    }

    public static void setItemEntityInRandomEnchantment(Level level, BlockPos pos) {
        AABB aabb = new AABB(pos).inflate(0.6D);
        level.getEntitiesOfClass(ItemEntity.class, aabb).forEach(entity -> {
            ItemStack stack = entity.getItem();
            stack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            getRandomEnchantment().forEach(enchantment -> stack.enchant(
                    enchantment, level.random.nextInt(Byte.MAX_VALUE)));
        });
    }

    public static void setEquipmentInRandomEnchantment(LivingEntity entity) {
        setItemInRandomEnchantment(entity.getItemInHand(entity.getUsedItemHand()));
        entity.getArmorSlots().forEach(StaffSpecialUtils::setItemInRandomEnchantment);
    }

    public static void setItemInRandomEnchantment(ItemStack itemStack) {
        itemStack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        getRandomEnchantment().forEach(enchantment -> itemStack.enchant(
                enchantment, RandomSource.create().nextInt(Byte.MAX_VALUE)));
    }

    public static List<Enchantment> getRandomEnchantment() {
        RandomSource random = RandomSource.create();
        List<Enchantment> list = new ArrayList<>();
        ForgeRegistries.ENCHANTMENTS.forEach(list::add);
        int count = random.nextInt(1, list.size());
        Collections.shuffle(list);
        return list.subList(0, count);
    }

    public static void setEntityGlint(Level level, Entity target) {
        if (target instanceof LivingEntity entity) {
            float pitch = RandomSource.create().nextFloat() * 0.1F + 0.9F;
            entity.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 2.0F, pitch);
            if (entity instanceof IHasEnchantmentGlintEntity glintEntity) {
                if (!level.isClientSide) {
                    glintEntity.setGlint(!glintEntity.isGlint());
                }
            }
        }
    }

}