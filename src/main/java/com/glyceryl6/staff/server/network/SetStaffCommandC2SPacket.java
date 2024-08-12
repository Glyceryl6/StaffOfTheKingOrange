package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.registry.KODataComponents;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetStaffCommandC2SPacket(String command) implements CustomPacketPayload {

    public static final Type<SetStaffCommandC2SPacket> TYPE = new Type<>(Main.prefix("set_staff_command"));
    public static final StreamCodec<ByteBuf, SetStaffCommandC2SPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SetStaffCommandC2SPacket::command, SetStaffCommandC2SPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetStaffCommandC2SPacket packet, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer player) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.getItem() instanceof StaffItem) {
                BlockState state = StaffUniversalUtils.getCoreBlockState(mainHandItem);
                DataComponentType<CustomData> type = KODataComponents.STAFF_BINDING_COMMAND.get();
                if (state.getBlock() instanceof CommandBlock) {
                    CompoundTag newCommand = CustomData.EMPTY.copyTag();
                    newCommand.putString("Command", packet.command);
                    mainHandItem.set(type, CustomData.of(newCommand));
                    if (!StringUtil.isNullOrEmpty(packet.command)) {
                        player.sendSystemMessage(Component.translatable("advMode.setCommand.success", packet.command));
                    }
                }
            }
        }
    }

}