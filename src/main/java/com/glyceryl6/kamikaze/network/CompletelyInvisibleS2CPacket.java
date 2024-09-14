package com.glyceryl6.kamikaze.network;

import com.glyceryl6.kamikaze.Main;
import com.glyceryl6.kamikaze.registry.KEAttachmentTypes;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CompletelyInvisibleS2CPacket(boolean completelyInvisible) implements CustomPacketPayload {

    public static final Type<CompletelyInvisibleS2CPacket> TYPE = new Type<>(Main.prefix("completely_invisible"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CompletelyInvisibleS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(CompletelyInvisibleS2CPacket::write, CompletelyInvisibleS2CPacket::new);

    public CompletelyInvisibleS2CPacket(FriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.completelyInvisible);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(CompletelyInvisibleS2CPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (context.flow().isClientbound() && player instanceof LocalPlayer) {
                player.setData(KEAttachmentTypes.COMPLETELY_INVISIBLE, packet.completelyInvisible);
            }
        });
    }

}