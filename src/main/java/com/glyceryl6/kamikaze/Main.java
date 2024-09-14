package com.glyceryl6.kamikaze;

import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class Main {

    public static final String MOD_ID = com.glyceryl6.staff.Main.MOD_ID;

    public static ResourceLocation prefix(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name.toLowerCase(Locale.ROOT));
    }

}