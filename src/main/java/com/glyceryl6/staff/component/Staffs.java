package com.glyceryl6.staff.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

public record Staffs(boolean isEffective, boolean continuousMode, int note) implements TooltipProvider {

    public static final Codec<Staffs> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("is_effective").forGetter(Staffs::isEffective),
            Codec.BOOL.fieldOf("continuous_mode").forGetter(Staffs::continuousMode),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("note").forGetter(Staffs::note)).apply(instance, Staffs::new));
    public static final StreamCodec<FriendlyByteBuf, Staffs> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, Staffs::isEffective,
            ByteBufCodecs.BOOL, Staffs::continuousMode,
            ByteBufCodecs.VAR_INT, Staffs::note, Staffs::new);

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        String key = "tooltip.staff.continuous_mode";
        Component component = Component.translatable(key + "." + this.continuousMode).withStyle(ChatFormatting.LIGHT_PURPLE);
        tooltipAdder.accept(Component.translatable(key).withStyle(ChatFormatting.AQUA).append(component));
    }

}