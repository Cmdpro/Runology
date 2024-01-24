package com.cmdpro.runology.api;

import com.cmdpro.runology.moddata.ChunkModDataProvider;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class RunologyUtil {
    public static Supplier<IForgeRegistry<RunicEnergyType>> RUNIC_ENERGY_TYPES_REGISTRY = null;
    public static Supplier<IForgeRegistry<InstabilityEvent>> INSTABILITY_EVENTS_REGISTRY = null;
    public static Supplier<IForgeRegistry<AnalyzeTaskSerializer>> ANALYZE_TASKS_REGISTRY = null;
    public static void AddInstability(ChunkPos chunk, Level level, float amount, float min, float max) {
        int[][] offsets = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 0 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };
        for (int[] i : offsets) {
            LevelChunk chunk2 = level.getChunk(chunk.x+i[0], chunk.z+i[1]);
            chunk2.getCapability(ChunkModDataProvider.CHUNK_MODDATA).ifPresent(data -> {
                data.setInstability(data.getInstability() + amount);
                if (data.getInstability() > max) {
                    data.setInstability(max);
                }
                if (data.getInstability() < min) {
                    data.setInstability(min);
                }
            });
        }
    }
    public static boolean playerHasEntry(Player player, String entry) {
        if (entry != null) {
            ConcurrentMap<ResourceLocation, Set<ResourceLocation>> entries = BookUnlockStateManager.get().saveData.getUnlockStates(player.getUUID()).unlockedEntries;
            for (Map.Entry<ResourceLocation, Set<ResourceLocation>> i : entries.entrySet()) {
                for (ResourceLocation o : i.getValue()) {
                    if (o.toString().equals(entry)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
