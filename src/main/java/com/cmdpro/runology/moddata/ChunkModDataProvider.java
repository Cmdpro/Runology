package com.cmdpro.runology.moddata;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChunkModDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ChunkModData> CHUNK_MODDATA = CapabilityManager.get(new CapabilityToken<ChunkModData>() { });
    private final LazyOptional<ChunkModData> optional = LazyOptional.of(this::createChunkData);
    private ChunkModData modData = null;
    public ChunkModData createChunkData() {
        if (this.modData == null) {
            this.modData = new ChunkModData();
        }
        return this.modData;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CHUNK_MODDATA) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createChunkData().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createChunkData().loadNBTData(nbt);
    }
}
