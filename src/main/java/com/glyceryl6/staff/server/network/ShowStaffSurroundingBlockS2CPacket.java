package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.registry.KODataComponents;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ShowStaffSurroundingBlockS2CPacket(int key) implements CustomPacketPayload {

    public static final Type<ShowStaffSurroundingBlockS2CPacket> TYPE = new Type<>(Main.prefix("show_staff_surrounding_block"));
    public static final StreamCodec<ByteBuf, ShowStaffSurroundingBlockS2CPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ShowStaffSurroundingBlockS2CPacket::key, ShowStaffSurroundingBlockS2CPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ShowStaffSurroundingBlockS2CPacket packet, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            if (itemInHand.getItem() instanceof StaffItem) {
                BlockState state = StaffUniversalUtils.getCoreBlockState(itemInHand);
                DataComponentType<CustomData> type = KODataComponents.STAFF_SURROUNDING_BLOCK.get();
                CustomData customData = itemInHand.get(type);
                if (state.is(Blocks.LAPIS_BLOCK) && customData != null) {
                    boolean show = customData.copyTag().getBoolean("Show");
                    String key = "message.staff.show_surrounding_block";
                    CompoundTag compound = CustomData.EMPTY.copyTag();
                    compound.putBoolean("Show", !show);
                    itemInHand.set(type, CustomData.of(compound));
                    MutableComponent component = Component.translatable(key);
                    player.displayClientMessage(component.append(String.valueOf(!show)), Boolean.TRUE);
                }
            }
        }
    }

}