package com.cmdpro.runology.moddata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;

public class ChunkModData {
    public ChunkModData() {

    }
    private float instability;
    public static final float MAX_INSTABILITY = 1000;
    private LevelChunk chunk;
    public LevelChunk getChunk() {
        return chunk;
    }
    public void setChunk(LevelChunk chunk) {
        this.chunk = chunk;
    }
    public float getInstability() {
        return instability;
    }
    public void setInstability(float amount) {
        this.instability = amount;
        chunk.setUnsaved(true);
    }
    public void addInstability(float amount) {
        this.instability += amount;
        chunk.setUnsaved(true);
    }
    public void saveNBTData(CompoundTag nbt) {
        nbt.putFloat("instability", instability);
    }
    public void loadNBTData(CompoundTag nbt) {
        this.instability = nbt.getFloat("instability");
    }
}
