package com.glyceryl6.kamikaze.event;

import com.glyceryl6.kamikaze.Main;
import com.glyceryl6.kamikaze.registry.KEAttachmentTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.level.ExplosionKnockbackEvent;

@EventBusSubscriber(modid = Main.MOD_ID)
public class LevelEventSubscriber {

    @SubscribeEvent
    public static void onExplosionKnockback(ExplosionKnockbackEvent event) {
        AttachmentType<Boolean> type = KEAttachmentTypes.IS_EXPLODER.get();
        Entity entity = event.getAffectedEntity();
        if (entity.getData(type)) {
            entity.setData(type, false);
            event.setKnockbackVelocity(Vec3.ZERO);
        }
    }

}