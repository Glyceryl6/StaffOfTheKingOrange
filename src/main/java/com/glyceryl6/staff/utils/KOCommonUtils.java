package com.glyceryl6.staff.utils;

import com.glyceryl6.staff.registry.KOBlocks;
import com.glyceryl6.staff.registry.KOItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.stream.Collectors;

public class KOCommonUtils {

    public static Iterable<Block> getKnownBlocks() {
        return KOBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
    }

    public static Iterable<Item> getKnownItems() {
        return KOItems.ITEMS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
    }

}