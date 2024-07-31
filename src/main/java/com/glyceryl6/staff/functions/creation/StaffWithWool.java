package com.glyceryl6.staff.functions.creation;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.world.item.context.UseOnContext;

public class StaffWithWool implements INormalStaffFunction {

    @Override
    public boolean canPlaceBlock(UseOnContext context) {
        return true;
    }

}