package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class KOCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NORMAL = CREATIVE_MODE_TABS.register("normal", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + Main.MOD_ID))
            .icon(() -> {
                ItemStack stack = new ItemStack(KOItems.STAFF);
                StaffUniversalUtils.setDefaultComponent(stack);
                return stack;
            }).displayItems((parameters, output) -> KOItems.ITEMS.getEntries().forEach(holder -> output.accept(holder.get()))).build());

}