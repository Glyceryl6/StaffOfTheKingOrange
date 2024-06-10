package com.glyceryl6.staff.functions.creation;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class StaffWithOakLog implements INormalStaffFunction {

    @Override
    public boolean canPlaceBlock(UseOnContext context) {
        return true;
    }

    @Override
    public List<Block> placeableBlocks() {
        List<Block> list = new ArrayList<>();
        list.add(Blocks.OAK_LOG);
        list.add(Blocks.OAK_PLANKS);
        list.add(Blocks.OAK_SLAB);
        list.add(Blocks.OAK_STAIRS);
        list.add(Blocks.OAK_WOOD);
        list.add(Blocks.OAK_FENCE);
        list.add(Blocks.STRIPPED_OAK_LOG);
        list.add(Blocks.STRIPPED_OAK_WOOD);
        return list;
    }

}