package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.registry.ModDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.network.CustomPayloadEvent;

/** @noinspection unused*/
public class SetStaffBlockC2SPacket {

    public SetStaffBlockC2SPacket(FriendlyByteBuf byteBuf) {}

    public SetStaffBlockC2SPacket() {}

    public void encode(FriendlyByteBuf byteBuf) {}

    public void handle(CustomPayloadEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player != null) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offhandItem = player.getOffhandItem();
            this.addItem(player, mainHandItem, offhandItem, InteractionHand.OFF_HAND);
            this.addItem(player, offhandItem, mainHandItem, InteractionHand.MAIN_HAND);
        }
    }

    private void addItem(ServerPlayer player, ItemStack mainHandItem, ItemStack offhandItem, InteractionHand hand) {
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