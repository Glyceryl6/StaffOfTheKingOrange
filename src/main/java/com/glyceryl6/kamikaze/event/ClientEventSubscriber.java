package com.glyceryl6.kamikaze.event;

import com.glyceryl6.kamikaze.Main;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public class ClientEventSubscriber {

    @SubscribeEvent
    public static void onRenderScreen(ScreenEvent.Init.Pre event) {
        if (event.getScreen() instanceof DeathScreen screen) {
            if (screen.causeOfDeath.getContents() instanceof TranslatableContents contents) {
                if (contents.getKey().equals("death.attack.explosion.suicide")) {
                    screen.title = Component.translatable("deathScreen.title.kamikaze");
                    screen.deathScore = Component.empty();
                }
            }
        }
    }

}