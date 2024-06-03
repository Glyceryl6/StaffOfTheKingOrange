package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/** @noinspection unused*/
public class RandomChangeStaffBlockC2SPacket {

    private static List<Block> blocks;

    public RandomChangeStaffBlockC2SPacket(FriendlyByteBuf byteBuf) {}

    public RandomChangeStaffBlockC2SPacket() {}

    public void encode(FriendlyByteBuf byteBuf) {}

    public void handle(CustomPayloadEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (blocks.isEmpty()) {
            blocks = allBlocks();
        }

        if (player != null && !player.isUsingItem()) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offhandItem = player.getOffhandItem();
            BlockState state = StaffUniversalUtils.getRandomBlockState(blocks.get(player.getRandom().nextInt(blocks.size())));
            if (mainHandItem.getItem() instanceof StaffItem && !(offhandItem.getItem() instanceof StaffItem)) {
                StaffUniversalUtils.setNormalBlockForStaff(mainHandItem, state);
            } else if (offhandItem.getItem() instanceof StaffItem && !(mainHandItem.getItem() instanceof StaffItem)) {
                StaffUniversalUtils.setNormalBlockForStaff(offhandItem, state);
            }
        }
    }

    private static List<Block> allBlocks() {
        List<Block> blockList = new ArrayList<>();
        ForgeRegistries.BLOCKS.forEach(blockList::add);
        return blockList;
    }

    static {
        blocks = allBlocks();
    }

}