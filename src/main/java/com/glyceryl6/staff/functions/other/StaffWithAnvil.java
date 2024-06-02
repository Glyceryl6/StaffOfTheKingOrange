package com.glyceryl6.staff.functions.other;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.google.common.base.Suppliers;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.UUID;
import java.util.function.Supplier;

public record StaffWithAnvil(double speed, double damage) implements INormalStaffFunction {

    @Override
    public boolean enableUse() {
        return false;
    }

    @Override
    public boolean enableUseTick() {
        return false;
    }

    @Override
    public ItemAttributeModifiers addAttributes(ItemStack stack) {
        Supplier<ItemAttributeModifiers> defaultModifiers = Suppliers.memoize(() -> {
            ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
            UUID uuid1 = UUID.fromString("ae200f51-95b5-4da0-94b3-5de80c0a3506");
            UUID uuid2 = UUID.fromString("8a8b7a3a-e47a-40e9-8c04-922a2158ca93");
            AttributeModifier.Operation operation1 = AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL;
            AttributeModifier.Operation operation2 = AttributeModifier.Operation.ADD_VALUE;
            AttributeModifier modifier1 = new AttributeModifier(uuid1, "Anvil: slow down", this.speed, operation1);
            AttributeModifier modifier2 = new AttributeModifier(uuid2, "Anvil: attack damage", this.damage, operation2);
            builder.add(Attributes.MOVEMENT_SPEED, modifier1, EquipmentSlotGroup.MAINHAND);
            builder.add(Attributes.ATTACK_DAMAGE, modifier2, EquipmentSlotGroup.MAINHAND);
            return builder.build();
        });

        return defaultModifiers.get();
    }

}