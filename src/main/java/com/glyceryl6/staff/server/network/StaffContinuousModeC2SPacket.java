package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.component.Staffs;
import com.glyceryl6.staff.registry.ModDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;

/** @noinspection unused*/
public class StaffContinuousModeC2SPacket {

    public StaffContinuousModeC2SPacket(FriendlyByteBuf byteBuf) {}

    public StaffContinuousModeC2SPacket() {}

    public void encode(FriendlyByteBuf byteBuf) {}

    public void handle(CustomPayloadEvent.Context context) {
        DataComponentType<Staffs> staffsType = ModDataComponents.STAFFS.get();
        ServerPlayer player = context.getSender();
        if (player != null) {
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