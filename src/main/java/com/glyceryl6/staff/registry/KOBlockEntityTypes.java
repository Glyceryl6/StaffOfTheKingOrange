package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.blocks.entity.SignalBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class KOBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Main.MOD_ID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SignalBlockEntity>> SIGNAL_BLOCK = BLOCK_ENTITY_TYPES.register("signal_block",
            () -> BlockEntityType.Builder.of(SignalBlockEntity::new, KOBlocks.SIGNAL_BLOCK.get()).build(null));

}