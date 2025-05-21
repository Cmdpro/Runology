package com.cmdpro.runology.commands;

import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.TestWorldGuiType;
import com.cmdpro.runology.api.RunologyUtil;
import com.cmdpro.runology.registry.WorldGuiRegistry;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;

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
                ).then(Commands.literal("guiTest")
                        .executes((command) -> {
                            return guitest(command);
                        })
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
    private static int guitest(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player) {
            Player player = (Player) command.getSource().getEntity();
            WorldGuiEntity entity = new WorldGuiEntity(player.level(), player.getEyePosition(), WorldGuiRegistry.TEST.get());
            player.level().addFreshEntity(entity);
        }
        return Command.SINGLE_SUCCESS;
    }
}
