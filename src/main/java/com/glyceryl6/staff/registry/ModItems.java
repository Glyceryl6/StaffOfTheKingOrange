package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.items.StaffItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);
    public static final RegistryObject<Item> STAFF = ITEMS.register("staff", StaffItem::new);

}