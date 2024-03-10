package com.cmdpro.runology.api;

import com.cmdpro.runology.init.DimensionInit;
import com.cmdpro.runology.integration.bookconditions.BookAnalyzeTaskCondition;
import com.cmdpro.runology.moddata.ChunkModData;
import com.cmdpro.runology.moddata.ChunkModDataProvider;
import com.klikli_dev.modonomicon.book.BookEntry;
import com.klikli_dev.modonomicon.book.conditions.BookAndCondition;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookOrCondition;
import com.klikli_dev.modonomicon.book.conditions.context.BookConditionContext;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.registries.IForgeRegistry;

import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class RunologyUtil {
    public static Supplier<IForgeRegistry<RunicEnergyType>> RUNIC_ENERGY_TYPES_REGISTRY = null;
    public static Supplier<IForgeRegistry<InstabilityEvent>> INSTABILITY_EVENTS_REGISTRY = null;
    public static Supplier<IForgeRegistry<AnalyzeTaskSerializer>> ANALYZE_TASKS_REGISTRY = null;
    public static Supplier<IForgeRegistry<Spell>> SPELL_REGISTRY = null;
    public static void AddInstability(ChunkPos chunk, Level level, float amount, float min, float max) {
        int[][] offsets = {
                { -2, -2 }, { -2, -1 }, { -2, 0 }, { -2, 1 }, { -2, 2 },
                { -1, -2 }, { -1, -1 }, { -1, 0 }, { -1, 1 }, { -1, 2 },
                { 0, -2 }, { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 },
                { 1, -2 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 1, 2 },
                { 2, -2 }, { 2, -1 }, { 2, 0 }, { 2, 1 }, { 2, 2 }
        };
        for (int[] i : offsets) {
            LevelChunk chunk2 = level.getChunk(chunk.x+i[0], chunk.z+i[1]);
            chunk2.getCapability(ChunkModDataProvider.CHUNK_MODDATA).ifPresent(data -> {
                data.setInstability(data.getInstability() + (amount/(new Vec2(i[0], i[1]).distanceToSqr(new Vec2(0, 0))+1)));
                if (data.getInstability() > max) {
                    data.setInstability(max);
                }
                if (data.getInstability() < min) {
                    data.setInstability(min);
                }
            });
        }
    }
    public static BookAnalyzeTaskCondition getAnalyzeCondition(BookCondition condition) {
        if (condition instanceof BookAnalyzeTaskCondition cond) {
            return cond;
        }
        if (condition instanceof BookAndCondition cond) {
            for (BookCondition i : cond.children()) {
                BookCondition cond2 = getAnalyzeCondition(i);
                if (cond2 instanceof BookAnalyzeTaskCondition cond3) {
                    return cond3;
                }
            }
        }
        if (condition instanceof BookOrCondition cond) {
            for (BookCondition i : cond.children()) {
                BookCondition cond2 = getAnalyzeCondition(i);
                if (cond2 instanceof BookAnalyzeTaskCondition cond3) {
                    return cond3;
                }
            }
        }
        return null;
    }
    public static boolean conditionAllTrueExceptAnalyze(BookEntry entry, Player player) {
        BookCondition condition = entry.getCondition();
        if (condition instanceof BookAndCondition cond) {
            for (BookCondition i : cond.children()) {
                if (!(i instanceof BookAnalyzeTaskCondition)) {
                    if (!i.test(BookConditionContext.of(entry.getBook(), entry), player)) {
                        return false;
                    }
                }
            }
            return true;
        }
        if (condition instanceof BookOrCondition cond) {
            for (BookCondition i : cond.children()) {
                if (!(i instanceof BookAnalyzeTaskCondition)) {
                    if (i.test(BookConditionContext.of(entry.getBook(), entry), player)) {
                        return true;
                    }
                }
            }
        }
        if (condition instanceof BookAnalyzeTaskCondition cond) {
            return true;
        }
        return condition.test(BookConditionContext.of(entry.getBook(), entry), player);
    }
    public static void RemoveInstability(ChunkPos chunk, Level level, float amount, float min, float max) {
        int[][] offsets = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };
        LevelChunk chunk2 = level.getChunk(chunk.x, chunk.z);
        chunk2.getCapability(ChunkModDataProvider.CHUNK_MODDATA).ifPresent(data -> {
            data.setInstability(data.getInstability() - amount);
            if (data.getInstability() > max) {
                data.setInstability(max);
            }
            if (data.getInstability() < min) {
                data.setInstability(min);
            }
            for (int[] i : offsets) {
                LevelChunk chunk3 = level.getChunk(chunk.x+i[0], chunk.z+i[1]);
                chunk3.getCapability(ChunkModDataProvider.CHUNK_MODDATA).ifPresent(data2 -> {
                    data2.setInstability(data2.getInstability()-amount);
                    if (data2.getInstability() > max) {
                        data2.setInstability(max);
                    }
                    if (data2.getInstability() < min) {
                        data2.setInstability(min);
                    }
                });
            }
        });
    }
    public static float getChunkInstability(ChunkPos chunk, Level level) {
        Optional<ChunkModData> data = level.getChunk(chunk.x, chunk.z).getCapability(ChunkModDataProvider.CHUNK_MODDATA).resolve();
        if (level.dimension().equals(DimensionInit.SHATTERREALM)) {
            return ChunkModData.MAX_INSTABILITY;
        }
        if (data.isPresent()) {
            return data.get().getInstability();
        }
        return 0;
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
