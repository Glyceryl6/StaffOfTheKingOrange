package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.server.network.RandomChangeStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.SetStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.StaffContinuousModeC2SPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ModNetworks {

    private static final SimpleChannel INSTANCE = ChannelBuilder
            .named(Main.prefix("main"))
            .serverAcceptedVersions((status, version) -> true)
            .clientAcceptedVersions((status, version) -> true)
            .networkProtocolVersion(1).simpleChannel();

    public static void register() {
        INSTANCE.messageBuilder(SetStaffBlockC2SPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetStaffBlockC2SPacket::encode).decoder(SetStaffBlockC2SPacket::new)
                .consumerNetworkThread(SetStaffBlockC2SPacket::handle).add();
        INSTANCE.messageBuilder(RandomChangeStaffBlockC2SPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(RandomChangeStaffBlockC2SPacket::encode).decoder(RandomChangeStaffBlockC2SPacket::new)
                .consumerNetworkThread(RandomChangeStaffBlockC2SPacket::handle).add();
        INSTANCE.messageBuilder(StaffContinuousModeC2SPacket.class, NetworkDirection.PLAY_TO_SERVER)
                .encoder(StaffContinuousModeC2SPacket::encode).decoder(StaffContinuousModeC2SPacket::new)
                .consumerNetworkThread(StaffContinuousModeC2SPacket::handle).add();
    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(msg, PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(msg, PacketDistributor.PLAYER.with(player));
    }

}