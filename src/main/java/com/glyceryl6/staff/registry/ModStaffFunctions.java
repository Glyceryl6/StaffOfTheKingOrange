package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.api.IPlayerHeadStaffFunction;
import com.glyceryl6.staff.event.ModEventFactory;
import com.glyceryl6.staff.functions.creation.StaffWithCobblestone;
import com.glyceryl6.staff.functions.creation.StaffWithCobweb;
import com.glyceryl6.staff.functions.creation.StaffWithOakLog;
import com.glyceryl6.staff.functions.creation.StaffWithWool;
import com.glyceryl6.staff.functions.destructive.StaffWithBedrock;
import com.glyceryl6.staff.functions.destructive.StaffWithDiamondBlock;
import com.glyceryl6.staff.functions.destructive.StaffWithNetheriteBlock;
import com.glyceryl6.staff.functions.offensive.*;
import com.glyceryl6.staff.functions.other.*;
import com.glyceryl6.staff.functions.player_head.StaffWithHerobrineHead;
import com.glyceryl6.staff.functions.utility.*;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class ModStaffFunctions {

    public static final Map<Block, INormalStaffFunction> NORMAL_STAFF_MAP = normalStaffFunctionMap();
    public static final Map<String, IPlayerHeadStaffFunction> PLAYER_HEAD_STAFF_MAP = playerHeadStaffFunctionMap();

    private static Map<Block, INormalStaffFunction> normalStaffFunctionMap() {
        Map<Block, INormalStaffFunction> map = new HashMap<>();
        BuiltInRegistries.BLOCK.getTagOrEmpty(BlockTags.WOOL).forEach(
                holder -> map.put(holder.get(), new StaffWithWool()));
        map.put(Blocks.FURNACE, new StaffWithFurnace(RecipeType.SMELTING));
        map.put(Blocks.SMOKER, new StaffWithFurnace(RecipeType.SMOKING));
        map.put(Blocks.BLAST_FURNACE, new StaffWithFurnace(RecipeType.BLASTING));
        map.put(Blocks.PACKED_ICE, new StaffWithIce(Blocks.PACKED_ICE));
        map.put(Blocks.BLUE_ICE, new StaffWithIce(Blocks.BLUE_ICE));
        map.put(Blocks.DAMAGED_ANVIL, new StaffWithAnvil(-0.3D, 20.0D));
        map.put(Blocks.CHIPPED_ANVIL, new StaffWithAnvil(-0.4D, 30.0D));
        map.put(Blocks.ANVIL, new StaffWithAnvil(-0.5D, 40.0D));
        map.put(Blocks.WITHER_SKELETON_SKULL, new StaffWithWitherSkull());
        map.put(Blocks.ENCHANTING_TABLE, new StaffWithEnchantingTable());
        map.put(Blocks.NETHERITE_BLOCK, new StaffWithNetheriteBlock());
        map.put(Blocks.REDSTONE_BLOCK, new StaffWithRedstoneBlock());
        map.put(Blocks.DIAMOND_BLOCK, new StaffWithDiamondBlock());
        map.put(Blocks.BREWING_STAND, new StaffWithBrewingStand());
        map.put(Blocks.LIGHTNING_ROD, new StaffWithLightningRod());
        map.put(Blocks.COBBLESTONE, new StaffWithCobblestone());
        map.put(Blocks.LAPIS_BLOCK, new StaffWithLapisBlock());
        map.put(Blocks.MAGMA_BLOCK, new StaffWithMagmaBlock());
        map.put(Blocks.NOTE_BLOCK, new StaffWithNoteBlock());
        map.put(Blocks.SNOW_BLOCK, new StaffWithSnowBlock());
        map.put(Blocks.BONE_BLOCK, new StaffWithBoneBlock());
        map.put(Blocks.BOOKSHELF, new StaffWithBookShelf());
        map.put(Blocks.BEE_NEST, new StaffWithBeeNest());
        map.put(Blocks.BEEHIVE, new StaffWithBeeNest());
        map.put(Blocks.BEDROCK, new StaffWithBedrock());
        map.put(Blocks.OAK_LOG, new StaffWithOakLog());
        map.put(Blocks.COBWEB, new StaffWithCobweb());
        map.put(Blocks.BELL, new StaffWithBell());
        map.put(Blocks.TNT, new StaffWithTnt());
        ModEventFactory.onRegisterNormalStaffFunction(map);
        return ImmutableMap.copyOf(map);
    }

    private static Map<String, IPlayerHeadStaffFunction> playerHeadStaffFunctionMap() {
        Map<String, IPlayerHeadStaffFunction> map = new HashMap<>();
        map.put("MHF_Herobrine", new StaffWithHerobrineHead());
        ModEventFactory.onRegisterPlayerHeadStaffFunction(map);
        return ImmutableMap.copyOf(map);
    }

}