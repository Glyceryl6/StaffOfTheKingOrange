package com.glyceryl6.staff.server.commands;

import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SetPlayerHeadBlockCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("player_head").requires(
                (commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("playerName", StringArgumentType.word())
                        .executes(commandSource -> setPlayerName(commandSource.getSource(),
                                StringArgumentType.getString(commandSource, "playerName"))));
    }

    private static int setPlayerName(CommandSourceStack source, String playerName) throws CommandSyntaxException {
        String key = "message.staff.player_head_change";
        ServerPlayer player = source.getPlayer();
        ServerLevel level = source.getLevel();
        if (player != null && !level.isClientSide) {
            String data = "minecraft:player_head[minecraft:profile=" + playerName + "]";
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            ItemParser parser = new ItemParser(level.registryAccess());
            ItemStack stack = Items.PLAYER_HEAD.getDefaultInstance();
            stack.applyComponents(parser.parse(new StringReader(data)).components());
            ResolvableProfile profile = stack.get(DataComponents.PROFILE);
            if (itemInHand.getItem() instanceof StaffItem) {
                BlockState state = Blocks.PLAYER_HEAD.defaultBlockState();
                StaffUniversalUtils.putCoreBlock(itemInHand, state);
                if (profile != null) {
                    itemInHand.set(DataComponents.PROFILE, profile);
                    source.sendSuccess(() -> Component.translatable(key, playerName), true);
                }
            }
        }

        return 0;
    }

}