package com.glyceryl6.staff.server.commands;

import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class SetNormalBlockCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandBuildContext context) {
        return Commands.literal("normal_block").requires(
                (commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("blockState", BlockStateArgument.block(context))
                        .executes(commandSource -> setBlockState(commandSource.getSource(),
                                BlockStateArgument.getBlock(commandSource, "blockState"))));
    }

    private static int setBlockState(CommandSourceStack source, BlockInput blockInput) {
        String key = "message.staff.normal_block_change";
        BlockState state = blockInput.getState();
        ServerPlayer player = source.getPlayer();
        ServerLevel level = source.getLevel();
        if (player != null && !level.isClientSide) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            if (itemInHand.getItem() instanceof StaffItem) {
                StaffUniversalUtils.putCoreBlock(itemInHand, state);
                String blockName = state.getBlock().getName().getString();
                source.sendSuccess(() -> Component.translatable(key, blockName), true);
            }
        }

        return 0;
    }

}