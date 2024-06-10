package com.glyceryl6.staff.utils;

import com.glyceryl6.staff.api.IAbstractStaffFunction;
import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.api.IPlayerHeadStaffFunction;
import com.glyceryl6.staff.registry.ModNormalStaffs;
import com.glyceryl6.staff.registry.ModPlayerHeadStaffs;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

@SuppressWarnings("deprecation")
public class StaffUniversalUtils {

    public static void setNormalBlockForStaff(ItemStack stack, BlockState state) {
        CompoundTag coreBlock = stack.getOrCreateTag();
        coreBlock.put("CoreBlock", NbtUtils.writeBlockState(state));
    }

    public static void setPlayerHeadForStaff(ItemStack itemInHand, String playerName) {
        ItemStack stack = Items.PLAYER_HEAD.getDefaultInstance();
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("SkullOwner", playerName);
        itemInHand.setTag(tag);
        setNormalBlockForStaff(itemInHand, Blocks.PLAYER_HEAD.defaultBlockState());
    }

    public static GameProfile getPlayerProfile(String playerName) {
        ItemStack stack = Items.PLAYER_HEAD.getDefaultInstance();
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("SkullOwner", playerName);
        return NbtUtils.readGameProfile(tag.getCompound("SkullOwner"));
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
        CompoundTag coreBlock = itemInHand.getOrCreateTag().getCompound("CoreBlock");
        return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), coreBlock);
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
        GameProfile profile = NbtUtils.readGameProfile(itemInHand.getOrCreateTag().getCompound("SkullOwner"));
        String name = profile != null ? profile.getName() : "";
        boolean flag = !name.isEmpty() && map.containsKey(name);
        return flag ? map.get(name) : new IPlayerHeadStaffFunction() {};
    }

}