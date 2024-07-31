package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.registry.ModDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetStaffBlockC2SPacket(int key) implements CustomPacketPayload {

    public static final Type<SetStaffBlockC2SPacket> TYPE = new Type<>(Main.prefix("set_staff_block"));
    public static final StreamCodec<ByteBuf, SetStaffBlockC2SPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SetStaffBlockC2SPacket::key, SetStaffBlockC2SPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void serverSideHandle(SetStaffBlockC2SPacket packet, IPayloadContext context) {
        Player player = context.player();
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offhandItem = player.getOffhandItem();
            addItem(serverPlayer, mainHandItem, offhandItem, InteractionHand.MAIN_HAND);
            addItem(serverPlayer, offhandItem, mainHandItem, InteractionHand.OFF_HAND);
        }
    }

    private static void addItem(ServerPlayer player, ItemStack mainHandItem, ItemStack offhandItem, InteractionHand hand) {
        if (mainHandItem.getItem() instanceof StaffItem && offhandItem.getItem() instanceof BlockItem blockItem) {
            DataComponentType<ResolvableProfile> profileType = DataComponents.PROFILE;
            CustomData customData = mainHandItem.get(ModDataComponents.STAFF_CORE_STATE.get());
            ResolvableProfile profile = offhandItem.get(profileType);
            if (customData != null) {
                Block itemBlock = blockItem.getBlock();
                CompoundTag coreBlock = customData.copyTag();
                BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), coreBlock);
                CompoundTag stateTag = NbtUtils.writeBlockState(itemBlock.defaultBlockState());
                boolean flag = itemBlock instanceof PlayerHeadBlock;
                if (state.getBlock() != itemBlock || flag) {
                    coreBlock.put("core_block", stateTag);
                    String key = "message.staff.normal_block_change";
                    String name = itemBlock.getName().getString();
                    if (flag && profile != null) {
                        mainHandItem.set(profileType, profile);
                        name = blockItem.getName(offhandItem).getString();
                    }

                    player.displayClientMessage(Component.translatable(key, name), Boolean.TRUE);
                    mainHandItem.set(ModDataComponents.STAFF_CORE_STATE.get(), CustomData.of(coreBlock));
                    offhandItem.consume(1, player);
                    player.swing(hand, Boolean.TRUE);
                }
            }
        }
    }

}