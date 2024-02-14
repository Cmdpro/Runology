package com.cmdpro.runology.commands;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.entity.RunicOverseer;
import com.cmdpro.runology.init.EntityInit;
import com.cmdpro.runology.moddata.ChunkModData;
import com.cmdpro.runology.moddata.ChunkModDataProvider;
import com.cmdpro.runology.moddata.PlayerModDataProvider;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.bookstate.BookVisualStateManager;
import com.klikli_dev.modonomicon.command.ResetBookUnlocksCommand;
import com.klikli_dev.modonomicon.data.BookDataManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;

public class RunologyCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal(Runology.MOD_ID)
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("resetlearned")
                        .executes((command) -> {
                            return resetlearned(command);
                        })
                )
                .then(Commands.literal("setchunkinstability")
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0, ChunkModData.MAX_INSTABILITY))
                                .executes((command) -> {
                                    return setchunkinstability(command);
                                })
                        )
                )
        );
    }

    private static int setchunkinstability(CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player) {
            Player player = (Player) command.getSource().getEntity();
            LevelChunk chunk = player.level().getChunkAt(player.blockPosition());
            chunk.getCapability(ChunkModDataProvider.CHUNK_MODDATA).ifPresent(data -> {
                float instability = command.getArgument("amount", float.class);
                if (ChunkModData.MAX_INSTABILITY >= instability) {
                    data.setInstability(instability);
                }
            });
        }
        return Command.SINGLE_SUCCESS;
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
}
