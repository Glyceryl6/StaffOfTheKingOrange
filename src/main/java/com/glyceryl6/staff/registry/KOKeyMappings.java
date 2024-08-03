package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class KOKeyMappings {

    public static final String CATEGORY_STAFF = "key.categories." + Main.MOD_ID;

    public static final KeyMapping ADD_REMOVE_KEYBINDING = new KeyMapping(
            "key.staff.add_remove_block", InputConstants.KEY_R, CATEGORY_STAFF);
    public static final KeyMapping RANDOM_CHANGE_KEYBINDING = new KeyMapping(
            "key.staff.random_change_block", InputConstants.KEY_B, CATEGORY_STAFF);
    public static final KeyMapping CONTINUOUS_MODE_KEYBINDING = new KeyMapping(
            "key.staff.continuous_mode", InputConstants.KEY_C, CATEGORY_STAFF);

}