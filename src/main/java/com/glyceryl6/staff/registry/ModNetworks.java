package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.server.network.RandomChangeStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.SetStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.StaffContinuousModeC2SPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworks {

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    private static final SimpleChannel INSTANCE =
            NetworkRegistry.ChannelBuilder.named(Main.prefix("main"))
            .serverAcceptedVersions(s -> true).clientAcceptedVersions(s -> true)
            .networkProtocolVersion(()-> NetworkConstants.NETVERSION).simpleChannel();

    public static void register() {
        INSTANCE.messageBuilder(SetStaffBlockC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(SetStaffBlockC2SPacket::encode).decoder(SetStaffBlockC2SPacket::new)
                .consumerNetworkThread(SetStaffBlockC2SPacket::handle).add();
        INSTANCE.messageBuilder(RandomChangeStaffBlockC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(RandomChangeStaffBlockC2SPacket::encode).decoder(RandomChangeStaffBlockC2SPacket::new)
                .consumerNetworkThread(RandomChangeStaffBlockC2SPacket::handle).add();
        INSTANCE.messageBuilder(StaffContinuousModeC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(StaffContinuousModeC2SPacket::encode).decoder(StaffContinuousModeC2SPacket::new)
                .consumerNetworkThread(StaffContinuousModeC2SPacket::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()-> player), message);
    }

}