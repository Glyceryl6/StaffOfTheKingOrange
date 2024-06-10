package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.common.items.StaffItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** @noinspection unused*/
public class StaffContinuousModeC2SPacket {

    public StaffContinuousModeC2SPacket(FriendlyByteBuf byteBuf) {}

    public StaffContinuousModeC2SPacket() {}

    public void encode(FriendlyByteBuf byteBuf) {}

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player != null) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.getItem() instanceof StaffItem) {
                CompoundTag tag = mainHandItem.getOrCreateTag();
                String key = "tooltip.staff.continuous_mode";
                boolean flag = !tag.getBoolean("ContinuousMode");
                tag.putBoolean("ContinuousMode", flag);
                MutableComponent component = Component.translatable(key + "." + flag);
                player.displayClientMessage(Component.translatable(key).append(component), Boolean.TRUE);
            }
        }
    }

}