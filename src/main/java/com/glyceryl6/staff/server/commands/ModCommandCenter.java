package com.glyceryl6.staff.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public class ModCommandCenter {

    public ModCommandCenter(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack> literal("ko_staff")
                .then(SetCoreBlockCommand.register(context)).executes(ctx -> 0));
    }

}