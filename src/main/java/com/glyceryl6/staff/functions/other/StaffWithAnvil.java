package com.glyceryl6.staff.functions.other;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

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
    public Multimap<Attribute, AttributeModifier> addAttributes(ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid1 = UUID.fromString("ae200f51-95b5-4da0-94b3-5de80c0a3506");
        UUID uuid2 = UUID.fromString("8a8b7a3a-e47a-40e9-8c04-922a2158ca93");
        AttributeModifier.Operation operation1 = AttributeModifier.Operation.MULTIPLY_TOTAL;
        AttributeModifier.Operation operation2 = AttributeModifier.Operation.ADDITION;
        AttributeModifier modifier1 = new AttributeModifier(uuid1, "Anvil: slow down", this.speed, operation1);
        AttributeModifier modifier2 = new AttributeModifier(uuid2, "Anvil: attack damage", this.damage, operation2);
        builder.put(Attributes.MOVEMENT_SPEED, modifier1);
        builder.put(Attributes.ATTACK_DAMAGE, modifier2);
        return builder.build();
    }

}