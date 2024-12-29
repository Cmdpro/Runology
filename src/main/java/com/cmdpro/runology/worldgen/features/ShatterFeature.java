package com.cmdpro.runology.worldgen.features;

import com.cmdpro.runology.registry.BlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ShatterFeature extends Feature<NoneFeatureConfiguration> {
    public ShatterFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        Vec3i offset = new Vec3i(8+pContext.random().nextIntBetweenInclusive(-8, 8), 0, 8+pContext.random().nextIntBetweenInclusive(-8, 8));
        BlockPos origin = pContext.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pContext.origin().offset(offset));
        if (!pContext.level().getBlockState(origin.below()).getFluidState().isEmpty()) {
            return false;
        }
        BlockPos shatterPos = origin.offset(0, 2, 0);
        if (pContext.level().getBlockState(shatterPos).canBeReplaced()) {
            setBlock(pContext.level(), shatterPos, BlockRegistry.SHATTER.get().defaultBlockState());
            return true;
        }
        return false;
    }
}
