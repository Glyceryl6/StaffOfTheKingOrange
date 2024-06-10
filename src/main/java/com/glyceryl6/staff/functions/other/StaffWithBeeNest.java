package com.glyceryl6.staff.functions.other;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.common.entities.Beeper;
import com.glyceryl6.staff.registry.ModEntityTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class StaffWithBeeNest implements INormalStaffFunction {

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        Beeper beeper = ModEntityTypes.BEEPER.get().create(level);
        Bee bee = EntityType.BEE.create(level);
        if (!level.isClientSide) {
            double x = player.getRandomX(0.5D);
            double z = player.getRandomZ(0.5D);
            Vec3 lookAngle = player.getLookAngle();
            if (level.random.nextFloat() <= 0.1F) {
                if (beeper != null) {
                    beeper.setPos(x, player.getY(), z);
                    beeper.setDeltaMovement(lookAngle);
                    level.addFreshEntity(beeper);
                }
            } else {
                if (bee != null) {
                    bee.setPos(x, player.getY(), z);
                    bee.setDeltaMovement(lookAngle);
                    level.addFreshEntity(bee);
                }
            }
        }
    }

    @Override
    public void useOnEntity(Player player, InteractionHand hand, Entity target) {
        this.attackEntity(player.level(), player, target);
    }

    @Override
    public void attackEntity(Level level, Player player, Entity target) {
        if (!level.isClientSide && target instanceof LivingEntity livingEntity && !(livingEntity instanceof Bee)) {
            AABB aabb = AABB.unitCubeFromLowerCorner(player.position()).inflate(64.0D, 10.0D, 64.0D);
            level.getEntitiesOfClass(Bee.class, aabb).forEach(entity -> entity.setTarget(livingEntity));
        }
    }

}