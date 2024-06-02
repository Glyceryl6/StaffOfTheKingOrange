package com.glyceryl6.staff.functions.other;

import com.glyceryl6.staff.common.entities.projectile.visible.ThrownItem;
import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class StaffWithBookShelf implements INormalStaffFunction {

    private static final Item[] ITEMS = new Item[]
            {Items.BOOK, Items.ENCHANTED_BOOK, Items.KNOWLEDGE_BOOK, Items.WRITABLE_BOOK,
            Items.WRITTEN_BOOK, Items.PAPER, Items.BOOKSHELF, Items.CHISELED_BOOKSHELF};

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        if (!level.isClientSide) {
            ThrownItem thrownItem = new ThrownItem(player, 0.0D, 0.0D, 0.0D, level);
            thrownItem.setPos(player.getRandomX(0.5D), player.getY(0.5D), player.getRandomZ(0.5D));
            thrownItem.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            thrownItem.setItem(ITEMS[level.random.nextInt(ITEMS.length)].getDefaultInstance());
            thrownItem.damageAmount = 1.5F;
            level.addFreshEntity(thrownItem);
        }
    }

}