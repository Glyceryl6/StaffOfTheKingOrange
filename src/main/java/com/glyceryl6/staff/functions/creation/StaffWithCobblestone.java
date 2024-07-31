package com.glyceryl6.staff.functions.creation;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class StaffWithCobblestone implements INormalStaffFunction {

    @Override
    public boolean canPlaceBlock(UseOnContext context) {
        return true;
    }

    @Override
    public List<Block> placeableBlocks() {
        List<Block> list = new ArrayList<>();
        list.add(Blocks.COBBLESTONE);
        list.add(Blocks.COBBLESTONE_WALL);
        list.add(Blocks.COBBLESTONE_SLAB);
        list.add(Blocks.COBBLESTONE_STAIRS);
        return list;
    }

}