package com.cmdpro.runology.api;

import com.cmdpro.runology.init.EntityInit;
import com.cmdpro.runology.moddata.ChunkModDataProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RunologyUtil {
    public static Supplier<IForgeRegistry<RunicEnergyType>> RUNIC_ENERGY_TYPES_REGISTRY = null;
    public static Supplier<IForgeRegistry<InstabilityEvent>> INSTABILITY_EVENTS_REGISTRY = null;
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
}
