package com.glyceryl6.staff.server.commands;

import com.glyceryl6.staff.common.items.StaffItem;
import com.glyceryl6.staff.utils.StaffUniversalUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class SetPlayerHeadBlockCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("player_head").requires(
                (commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("playerName", StringArgumentType.word())
                        .executes(commandSource -> setPlayerName(commandSource.getSource(),
                                StringArgumentType.getString(commandSource, "playerName"))));
    }

    private static int setPlayerName(CommandSourceStack source, String playerName) {
        String key = "message.staff.player_head_change";
        ServerPlayer player = source.getPlayer();
        ServerLevel level = source.getLevel();
        if (player != null && !level.isClientSide) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            if (itemInHand.getItem() instanceof StaffItem) {
                StaffUniversalUtils.setPlayerHeadForStaff(level, itemInHand, playerName);
                source.sendSuccess(() -> Component.translatable(key, playerName), Boolean.TRUE);
            }
        }

        return 0;
    }

}