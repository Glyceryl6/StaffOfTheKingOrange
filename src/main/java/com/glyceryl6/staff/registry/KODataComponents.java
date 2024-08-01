package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.component.Staffs;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** @noinspection deprecation*/
public class KODataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Main.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CustomData>> STAFF_CORE_STATE = DATA_COMPONENT_TYPE.register("staff_core_state",
            () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).cacheEncoding().build());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Staffs>> STAFFS = DATA_COMPONENT_TYPE.register("staffs",
            () -> DataComponentType.<Staffs>builder().persistent(Staffs.CODEC).networkSynchronized(Staffs.STREAM_CODEC).cacheEncoding().build());

}