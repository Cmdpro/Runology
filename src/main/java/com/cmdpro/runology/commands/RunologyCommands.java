package com.cmdpro.runology.commands;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.moddata.PlayerModDataProvider;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.data.BookDataManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class RunologyCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal(Runology.MOD_ID)
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("setrunicknowledge")
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes((command) -> {
                                    return setrunicknowledge(command);
                                })
                        )
                )
                .then(Commands.literal("resetlearned")
                        .executes((command) -> {
                            return resetlearned(command);
                        })
                )
        );
    }
    private static int resetlearned(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player) {
            Player player = (Player) command.getSource().getEntity();
            player.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                data.getUnlocked().clear();
                BookUnlockStateManager.get().resetFor((ServerPlayer)player, BookDataManager.get().getBook(new ResourceLocation("runology", "runologyguide")));
                BookUnlockStateManager.get().updateAndSyncFor((ServerPlayer)player);
            });
        }
        return Command.SINGLE_SUCCESS;
    }
    private static int setrunicknowledge(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player) {
            Player player = (Player) command.getSource().getEntity();
            player.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent(data -> {
                int knowledge = command.getArgument("amount", int.class);
                data.setRunicKnowledge(knowledge);
            });
        }
        return Command.SINGLE_SUCCESS;
    }
}
