package com.glyceryl6.staff.mixin;

import com.glyceryl6.kamikaze.registry.KEEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class MixinEnchantment {

    @Inject(method = "getFullname", at = @At(value = "HEAD"), cancellable = true)
    private static void getFullname(
            Holder<Enchantment> enchantment, int level,
            CallbackInfoReturnable<Component> cir) {
        MutableComponent component = enchantment.value().description().copy();
        if (enchantment.is(KEEnchantments.KAMIKAZE_ELYTRA)) {
            cir.setReturnValue(component.withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD));
        }
    }

}