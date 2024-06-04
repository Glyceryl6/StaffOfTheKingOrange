package com.glyceryl6.staff.handler;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.registry.ModKeyMappings;
import com.glyceryl6.staff.registry.ModMobEffects;
import com.glyceryl6.staff.registry.ModNetworks;
import com.glyceryl6.staff.server.network.RandomChangeStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.SetStaffBlockC2SPacket;
import com.glyceryl6.staff.server.network.StaffContinuousModeC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
public class ForgeClientHandler {

    @SubscribeEvent
    public static void onMovementInputUpdate(MovementInputUpdateEvent event) {
        Optional<Holder<MobEffect>> holder = ModMobEffects.STUN.getHolder();
        if (holder.isPresent() && event.getEntity().hasEffect(holder.get())) {
            Input input = event.getInput();
            input.up = false;
            input.down = false;
            input.left = false;
            input.right = false;
            input.forwardImpulse = 0.0F;
            input.leftImpulse = 0.0F;
            input.jumping = false;
            input.shiftKeyDown = false;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player != null) {
            if (ModKeyMappings.RANDOM_CHANGE_KEYBINDING.consumeClick()) {
                ModNetworks.sendToServer(new RandomChangeStaffBlockC2SPacket());
            }

            if (ModKeyMappings.ADD_REMOVE_KEYBINDING.consumeClick()) {
                ModNetworks.sendToServer(new SetStaffBlockC2SPacket());
            }

            if (ModKeyMappings.CONTINUOUS_MODE_KEYBINDING.consumeClick()) {
                ModNetworks.sendToServer(new StaffContinuousModeC2SPacket());
            }
        }
    }

}