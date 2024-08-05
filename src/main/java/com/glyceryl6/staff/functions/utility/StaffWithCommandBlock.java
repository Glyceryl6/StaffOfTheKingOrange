package com.glyceryl6.staff.functions.utility;

import com.glyceryl6.staff.api.INormalStaffFunction;
import com.glyceryl6.staff.client.gui.StaffCommandBlockEditScreen;
import com.glyceryl6.staff.registry.KODataComponents;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.apache.commons.lang3.StringUtils;

public class StaffWithCommandBlock implements INormalStaffFunction {

    public String normalizeChatMessage(String message) {
        return StringUtil.trimChatMessage(StringUtils.normalizeSpace(message.trim()));
    }

    @Override
    public void use(Level level, Player player, ItemStack stack) {
        CustomData customData = stack.get(KODataComponents.STAFF_COMMAND.get());
        if (customData != null && player instanceof LocalPlayer localPlayer) {
            String command = customData.copyTag().getString("Command");
            if (player.isShiftKeyDown()) {
                StaffCommandBlockEditScreen screen = new StaffCommandBlockEditScreen();
                screen.setCommandContent(command);
                localPlayer.minecraft.setScreen(screen);
            } else {
                String message = this.normalizeChatMessage(command);
                if (!message.isEmpty()) {
                    ClientPacketListener connection = localPlayer.connection;
                    if (message.startsWith("/")) {
                        connection.sendCommand(message.substring(1));
                    } else {
                        connection.sendChat(message);
                    }
                }
            }
        }
    }

    @Override
    public void useTick(Level level, Player player, ItemStack stack) {
        CustomData customData = stack.get(KODataComponents.STAFF_COMMAND.get());
        if (customData != null && player instanceof ServerPlayer serverPlayer) {
            String command = customData.copyTag().getString("Command");
            String message = this.normalizeChatMessage(command);
            Vec2 rotationVector = player.getRotationVector();
            ServerLevel serverLevel = level instanceof ServerLevel ? (ServerLevel) level : null;
            int permissionLevel = serverPlayer.server.getProfilePermissions(serverPlayer.getGameProfile());
            String textName = player.getName().getString();
            Component displayName = player.getDisplayName();
            MinecraftServer server = level.getServer();
            if (serverLevel != null && displayName != null && server != null) {
                CommandSourceStack sourceStack = new CommandSourceStack(
                        player, player.position(), rotationVector, serverLevel,
                        permissionLevel, textName, displayName, server, player);
                server.getCommands().performPrefixedCommand(sourceStack, message);
            }
        }
    }

    @Override
    public void useOnBlock(UseOnContext context) {
        this.useTick(context.getLevel(), context.getPlayer(), context.getItemInHand());
    }

}