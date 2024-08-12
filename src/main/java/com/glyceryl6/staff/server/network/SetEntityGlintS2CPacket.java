package com.glyceryl6.staff.server.network;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.registry.KOAttachmentTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetEntityGlintS2CPacket(int entityID) implements CustomPacketPayload {

    public static final Type<SetEntityGlintS2CPacket> TYPE = new Type<>(Main.prefix("set_entity_glint"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetEntityGlintS2CPacket> STREAM_CODEC =
            CustomPacketPayload.codec(SetEntityGlintS2CPacket::write, SetEntityGlintS2CPacket::new);

    public SetEntityGlintS2CPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetEntityGlintS2CPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = context.player().level().getEntity(packet.entityID);
            if (entity instanceof LivingEntity livingEntity) {
                AttachmentType<Boolean> type = KOAttachmentTypes.ENCHANTMENT_GLINT_OVERLAY.get();
                livingEntity.setData(type, !livingEntity.getData(type));
            }
        });
    }

}