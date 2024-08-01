package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.items.StaffItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class KOItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Main.MOD_ID);
    public static final DeferredHolder<Item, Item> STAFF = ITEMS.register("staff", StaffItem::new);

}