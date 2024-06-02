package com.glyceryl6.staff.functions.destructive;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.function.Supplier;

public class StaffWithNetheriteBlock implements INormalStaffFunction {

    @Override
    public boolean enableUse() {
        return false;
    }

    @Override
    public boolean enableUseTick() {
        return false;
    }

    @Override
    public void attackBlock(Level level, Player player, BlockPos pos) {
        BlockPos pos1 = pos.relative(player.getDirection(), 7);
        for (int x = -7; x <= 7; x++) {
            for (int y = -7; y <= 7; y++) {
                for (int z = -7; z <= 7; z++) {
                    BlockPos pos2 = pos1.offset(x, y, z);
                    level.destroyBlock(pos2, Boolean.FALSE);
                }
            }
        }
    }

    @Override
    public void attackEntity(Level level, Player player, Entity target) {
        if (!level.isClientSide && target instanceof LivingEntity entity) {
            float f = (float) (Math.PI / 180.0D);
            double rx = Mth.sin(player.getYRot() * f);
            double rz = -Mth.cos(player.getYRot() * f);
            entity.hasImpulse = true;
            Vec3 vec3 = entity.getDeltaMovement();
            Vec3 vec31 = new Vec3(rx, 0.0D, rz).normalize().scale(10.0D);
            double y = entity.onGround() ? Math.min(0.4D, vec3.y / 2.0D + 10.0D) : vec3.y;
            entity.setDeltaMovement(vec3.x / 2.0D - vec31.x, y, vec3.z / 2.0D - vec31.z);
        }
    }

    @Override
    public ItemAttributeModifiers addAttributes(EquipmentSlot slot, ItemStack stack) {
        Supplier<ItemAttributeModifiers> defaultModifiers = Suppliers.memoize(() -> {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            UUID uuid = UUID.fromString("ca9e513f-3a7e-401a-8d8a-f1196b958de7");
            String name = "Netherite: attack damage";
            AttributeModifier.Operation operation = AttributeModifier.Operation.ADD_VALUE;
            AttributeModifier modifier = new AttributeModifier(uuid, name, 10.0D, operation);
            builder.add(Attributes.ATTACK_DAMAGE, modifier, EquipmentSlotGroup.MAINHAND);
            return builder.build();
        });

        return defaultModifiers.get();
    }

}