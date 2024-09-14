package com.glyceryl6.kamikaze.event;

import com.glyceryl6.kamikaze.Main;
import com.glyceryl6.kamikaze.network.CompletelyInvisibleS2CPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusEventSubscriber {

    @SubscribeEvent
    public static void registerNetworks(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(CompletelyInvisibleS2CPacket.TYPE,
                CompletelyInvisibleS2CPacket.STREAM_CODEC,
                CompletelyInvisibleS2CPacket::handle);
    }

}