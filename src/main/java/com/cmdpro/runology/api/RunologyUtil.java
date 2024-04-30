package com.cmdpro.runology.api;

import com.cmdpro.runology.init.DimensionInit;
import com.cmdpro.runology.integration.modonomicon.bookconditions.BookAnalyzeTaskCondition;
import com.cmdpro.runology.moddata.ChunkModData;
import com.cmdpro.runology.moddata.ChunkModDataProvider;
import com.cmdpro.runology.networking.ModMessages;
import com.cmdpro.runology.networking.packet.DisplayEnderTransporterParticleLineS2CPacket;
import com.cmdpro.runology.networking.packet.DisplayInstabilityGenerationS2CPacket;
import com.klikli_dev.modonomicon.book.BookEntry;
import com.klikli_dev.modonomicon.book.conditions.BookAndCondition;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookOrCondition;
import com.klikli_dev.modonomicon.book.conditions.context.BookConditionContext;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

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
    public static void drawLine(ParticleOptions particle, Vec3 point1, Vec3 point2, Level level, double space) {
        double distance = point1.distanceTo(point2);
        Vec3 vector = point2.subtract(point1).normalize().multiply(space, space, space);
        double length = 0;
        for (Vec3 point = point1; length < distance; point = point.add(vector)) {
            ((ServerLevel)level).sendParticles(particle, point.x, point.y, point.z, 1, 0, 0, 0, 0);
            length += space;
        }
    }
    public static void displayInstabilityGen(Level level, Vec3 pos, Vec3 dir, float instabilityAmount) {
        DisplayInstabilityGenerationS2CPacket packet = new DisplayInstabilityGenerationS2CPacket(pos, dir, instabilityAmount);
        for(int j = 0; j < ((ServerLevel)level).players().size(); ++j) {
            ServerPlayer serverplayer = ((ServerLevel)level).players().get(j);
            if (serverplayer.level() == ((ServerLevel)level)) {
                BlockPos blockpos = serverplayer.blockPosition();
                if (blockpos.closerToCenterThan(pos, 32.0D)) {
                    ModMessages.sendToPlayer(packet, serverplayer);
                }
            }
        }
    }
}
