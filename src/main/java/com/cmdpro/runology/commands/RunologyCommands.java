package com.cmdpro.runology.commands;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.RunologyUtil;
import com.cmdpro.runology.registry.AttachmentTypeRegistry;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class RunologyCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal(Runology.MODID)
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("set_power_mode")
                        .then(Commands.argument("active", BoolArgumentType.bool())
                            .executes((command) -> {
                                return setpowermode(command);
                            })
                        )
                )
        );
    }
    private static int setpowermode(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player) {
            Player player = (Player) command.getSource().getEntity();
            boolean active = command.getArgument("active", Boolean.class);
            if (active) {
                RunologyUtil.activatePowerMode(player);
            } else {
                RunologyUtil.deactivatePowerMode(player);
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
