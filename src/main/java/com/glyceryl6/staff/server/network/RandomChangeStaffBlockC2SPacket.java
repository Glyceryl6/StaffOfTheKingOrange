package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record RandomChangeStaffBlockC2SPacket(int key) implements CustomPacketPayload {

    public static final Type<RandomChangeStaffBlockC2SPacket> TYPE = new Type<>(Main.prefix("random_change_staff_block"));
    public static final StreamCodec<ByteBuf, RandomChangeStaffBlockC2SPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, RandomChangeStaffBlockC2SPacket::key, RandomChangeStaffBlockC2SPacket::new);
    private static List<Block> blocks;

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(RandomChangeStaffBlockC2SPacket packet, IPayloadContext context) {
        Player player = context.player();
        if (blocks.isEmpty()) {
            blocks = allBlocks();
        }

        if (player instanceof ServerPlayer && !player.isUsingItem()) {
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
        BuiltInRegistries.BLOCK.forEach(blockList::add);
        return blockList;
    }

    static {
        blocks = allBlocks();
    }

}