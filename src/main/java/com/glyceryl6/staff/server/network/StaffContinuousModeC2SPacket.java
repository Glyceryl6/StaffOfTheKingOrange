package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.component.Staffs;
import com.glyceryl6.staff.registry.KODataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record StaffContinuousModeC2SPacket(int key) implements CustomPacketPayload {

    public static final Type<StaffContinuousModeC2SPacket> TYPE = new Type<>(Main.prefix("staff_continuous_mode"));
    public static final StreamCodec<ByteBuf, StaffContinuousModeC2SPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, StaffContinuousModeC2SPacket::key, StaffContinuousModeC2SPacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void serverSideHandle(StaffContinuousModeC2SPacket packet, IPayloadContext context) {
        DataComponentType<Staffs> staffsType = KODataComponents.STAFFS.get();
        Player player = context.player();
        if (player instanceof ServerPlayer) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.getItem() instanceof StaffItem) {
                Staffs staffs = mainHandItem.get(staffsType);
                if (staffs != null) {
                    String key = "tooltip.staff.continuous_mode";
                    boolean flag = !staffs.continuousMode();
                    mainHandItem.set(staffsType, new Staffs(staffs.isEffective(), flag, staffs.note()));
                    MutableComponent component = Component.translatable(key + "." + flag);
                    player.displayClientMessage(Component.translatable(key).append(component), Boolean.TRUE);
                }
            }
        }
    }

}