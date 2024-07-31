package com.glyceryl6.staff.registry;

import com.glyceryl6.staff.Main;
import com.glyceryl6.staff.common.blocks.SignalBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Main.MOD_ID);
    public static final DeferredHolder<Block, Block> SIGNAL_BLOCK = BLOCKS.register("signal_block",
            () -> new SignalBlock(BlockBehaviour.Properties.of().noOcclusion().noTerrainParticles().replaceable()));

}