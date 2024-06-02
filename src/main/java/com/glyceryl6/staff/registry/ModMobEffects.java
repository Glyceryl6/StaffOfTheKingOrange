package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.effects.StunEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Main.MOD_ID);
    public static final RegistryObject<MobEffect> STUN = MOB_EFFECTS.register("stun", StunEffect::new);

}