package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.blocks.entity.SignalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MOD_ID);
    public static final RegistryObject<BlockEntityType<SignalBlockEntity>> SIGNAL_BLOCK = BLOCK_ENTITY_TYPES.register("signal_block",
            () -> BlockEntityType.Builder.of(SignalBlockEntity::new, ModBlocks.SIGNAL_BLOCK.get()).build(null));

}