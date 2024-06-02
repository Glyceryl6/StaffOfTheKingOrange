package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.api.IPlayerHeadStaffFunction;
import com.glyceryl6.staff.event.ModEventFactory;
import com.glyceryl6.staff.functions.player_head.StaffWithHerobrineHead;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class ModPlayerHeadStaffs {

    public static final Map<String, IPlayerHeadStaffFunction> PLAYER_HEAD_STAFF_MAP = playerHeadStaffFunctionMap();

    private static Map<String, IPlayerHeadStaffFunction> playerHeadStaffFunctionMap() {
        Map<String, IPlayerHeadStaffFunction> map = new HashMap<>();
        map.put("MHF_Herobrine", new StaffWithHerobrineHead());
        ModEventFactory.onRegisterPlayerHeadStaffFunction(map);
        return ImmutableMap.copyOf(map);
    }

}