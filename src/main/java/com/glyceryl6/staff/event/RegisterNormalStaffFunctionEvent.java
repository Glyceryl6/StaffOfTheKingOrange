package com.glyceryl6.staff.event;

import com.glyceryl6.staff.api.INormalStaffFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PlayerHeadBlock;
import net.minecraftforge.eventbus.api.Event;

import java.util.Map;

public class RegisterNormalStaffFunctionEvent extends Event {

    private final Map<Block, INormalStaffFunction> staffFunctionMap;

    public RegisterNormalStaffFunctionEvent(Map<Block, INormalStaffFunction> staffFunctionMap) {
        this.staffFunctionMap = staffFunctionMap;
    }

    public void register(Block block, INormalStaffFunction function) {
        if (this.staffFunctionMap.containsKey(block)) {
            throw new IllegalArgumentException("Duplicate block key: " + BuiltInRegistries.BLOCK.getKey(block));
        } else if (block instanceof PlayerHeadBlock) {
            throw new IllegalArgumentException("Owing to the specificity of player head, please use event to register it!");
        } else {
            this.staffFunctionMap.put(block, function);
        }
    }

}