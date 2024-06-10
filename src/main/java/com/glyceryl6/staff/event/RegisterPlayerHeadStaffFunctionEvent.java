package com.glyceryl6.staff.event;

import com.glyceryl6.staff.api.IPlayerHeadStaffFunction;
import net.minecraftforge.eventbus.api.Event;

import java.util.Map;

public class RegisterPlayerHeadStaffFunctionEvent extends Event {

    private final Map<String, IPlayerHeadStaffFunction> staffFunctionMap;

    public RegisterPlayerHeadStaffFunctionEvent(Map<String, IPlayerHeadStaffFunction> staffFunctionMap) {
        this.staffFunctionMap = staffFunctionMap;
    }

    public void register(String name, IPlayerHeadStaffFunction function) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The name cannot be empty!");
        } else {
            this.staffFunctionMap.put(name, function);
        }
    }

}