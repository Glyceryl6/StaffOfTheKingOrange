package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class KOCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Main.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NORMAL = CREATIVE_MODE_TABS.register("normal", () -> CreativeModeTab
            .builder().title(Component.translatable("itemGroup." + Main.MOD_ID)).icon(() -> KOItems.STAFF.get().getDefaultInstance())
            .displayItems((parameters, output) -> KOItems.ITEMS.getEntries().forEach(holder -> output.accept(holder.get()))).build());

}