package com.glyceryl6.staff.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ModKeyMappings {

    public static final KeyMapping ADD_REMOVE_KEYBINDING = new KeyMapping(
            "key.staff.add_remove_block", InputConstants.KEY_R, KeyMapping.CATEGORY_MISC);
    public static final KeyMapping RANDOM_CHANGE_KEYBINDING = new KeyMapping(
            "key.staff.random_change_block", InputConstants.KEY_B, KeyMapping.CATEGORY_MISC);
    public static final KeyMapping CONTINUOUS_MODE_KEYBINDING = new KeyMapping(
            "key.staff.continuous_mode", InputConstants.KEY_C, KeyMapping.CATEGORY_MISC);

}