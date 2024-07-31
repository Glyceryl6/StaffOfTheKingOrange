package com.glyceryl6.staff.utils;

import com.glyceryl6.staff.api.IAbstractStaffFunction;
import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.api.IPlayerHeadStaffFunction;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.registry.ModDataComponents;
import com.glyceryl6.staff.registry.ModNormalStaffs;
import com.glyceryl6.staff.registry.ModPlayerHeadStaffs;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StaffUniversalUtils {

    public static void setNormalBlockForStaff(ItemStack stack, BlockState state) {
        CompoundTag coreBlock = CustomData.EMPTY.copyTag();
        coreBlock.put("core_block", NbtUtils.writeBlockState(state));
        stack.set(ModDataComponents.STAFF_CORE_STATE.get(), CustomData.of(coreBlock));
    }

    public static void setPlayerHeadForStaff(Level level, ItemStack itemInHand, String playerName) {
        ResolvableProfile profile = getPlayerProfile(level, playerName);
        if (itemInHand.getItem() instanceof StaffItem && profile != null) {
            setNormalBlockForStaff(itemInHand, Blocks.PLAYER_HEAD.defaultBlockState());
            itemInHand.set(DataComponents.PROFILE, profile);
        }
    }

    public static ResolvableProfile getPlayerProfile(Level level, String playerName) {
        String data = "minecraft:player_head[minecraft:profile=" + playerName + "]";
        ItemParser parser = new ItemParser(level.registryAccess());
        ItemStack stack = Items.PLAYER_HEAD.getDefaultInstance();
        try {
            stack.applyComponents(parser.parse(new StringReader(data)).components());
        } catch (CommandSyntaxException ignored) {}
        return stack.get(DataComponents.PROFILE);
    }

    public static BlockState getRandomBlockState(Block block) {
        RandomSource random = RandomSource.create();
        BlockState state = block.defaultBlockState();
        Holder<Block> holder = state.getBlockHolder();
        StateDefinition<Block, BlockState> stateDefinition = holder.value().getStateDefinition();
        Collection<Property<?>> properties = stateDefinition.getProperties();
        if (!properties.isEmpty()) {
            List<Property<?>> propertyList = new ArrayList<>(properties);
            Property<?> property = propertyList.get(random.nextInt(propertyList.size()));
            return DebugStickItem.cycleState(state, property, random.nextBoolean());
        } else {
            return state;
        }
    }

    public static BlockState getCoreBlockState(ItemStack itemInHand) {
        CustomData customData = itemInHand.get(ModDataComponents.STAFF_CORE_STATE.get());
        if (customData != null) {
            CompoundTag coreBlock = customData.copyTag().getCompound("core_block");
            return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), coreBlock);
        }

        return Blocks.COMMAND_BLOCK.defaultBlockState();
    }

    public static IAbstractStaffFunction getStaffFunction(ItemStack itemInHand) {
        Block coreBlock = getCoreBlockState(itemInHand).getBlock();
        IPlayerHeadStaffFunction function1 = getPlayerHeadStaffFunction(itemInHand);
        INormalStaffFunction function2 = getNormalStaffFunction(itemInHand);
        return coreBlock instanceof PlayerHeadBlock ? function1 : function2;
    }

    public static INormalStaffFunction getNormalStaffFunction(ItemStack itemInHand) {
        Map<Block, INormalStaffFunction> map = ModNormalStaffs.NORMAL_STAFF_MAP;
        Block block = getCoreBlockState(itemInHand).getBlock();
        return map.containsKey(block) ? map.get(block) : new INormalStaffFunction() {};
    }

    public static IPlayerHeadStaffFunction getPlayerHeadStaffFunction(ItemStack itemInHand) {
        Map<String, IPlayerHeadStaffFunction> map = ModPlayerHeadStaffs.PLAYER_HEAD_STAFF_MAP;
        ResolvableProfile profile = itemInHand.get(DataComponents.PROFILE);
        String name = profile != null ? profile.name().orElse("") : "";
        boolean flag = !name.isEmpty() && map.containsKey(name);
        return flag ? map.get(name) : new IPlayerHeadStaffFunction() {};
    }

}