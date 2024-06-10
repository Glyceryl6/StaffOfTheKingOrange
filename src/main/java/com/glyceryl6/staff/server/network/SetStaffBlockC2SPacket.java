package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.common.items.StaffItem;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** @noinspection unused*/
public class SetStaffBlockC2SPacket {

    public SetStaffBlockC2SPacket(FriendlyByteBuf byteBuf) {}

    public SetStaffBlockC2SPacket() {}

    public void encode(FriendlyByteBuf byteBuf) {}

    public void handle(Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player != null) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offhandItem = player.getOffhandItem();
            addItem(player, mainHandItem, offhandItem, InteractionHand.OFF_HAND);
            addItem(player, offhandItem, mainHandItem, InteractionHand.MAIN_HAND);
        }
    }

    private static void addItem(ServerPlayer player, ItemStack mainHandItem, ItemStack offhandItem, InteractionHand hand) {
        if (mainHandItem.getItem() instanceof StaffItem && offhandItem.getItem() instanceof BlockItem blockItem) {
            Block itemBlock = blockItem.getBlock();
            CompoundTag mainHandTag = mainHandItem.getOrCreateTag();
            CompoundTag offHandTag = offhandItem.getOrCreateTag();
            GameProfile profile = NbtUtils.readGameProfile(offHandTag.getCompound("SkullOwner"));
            BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), mainHandTag);
            CompoundTag stateTag = NbtUtils.writeBlockState(itemBlock.defaultBlockState());
            boolean flag = itemBlock instanceof PlayerHeadBlock;
            if (state.getBlock() != itemBlock || flag) {
                mainHandTag.put("CoreBlock", stateTag);
                String key = "message.staff.normal_block_change";
                String name = itemBlock.getName().getString();
                if (flag && profile != null) {
                    mainHandTag.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), profile));
                    name = blockItem.getName(offhandItem).getString();
                }

                player.displayClientMessage(Component.translatable(key, name), Boolean.TRUE);
                player.swing(hand, Boolean.TRUE);
                if (!player.getAbilities().instabuild) {
                    offhandItem.shrink(1);
                }
            }
        }
    }

}