package com.cmdpro.runology.worldgen.structures;

import com.cmdpro.runology.init.StructureInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.EndCityStructure;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Optional;

public class ShatterRealmStructure extends Structure {
    public static final int MAX_TOTAL_STRUCTURE_RANGE = 128;
    public static final Codec<ShatterRealmStructure> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec((p_227640_) -> {
        return p_227640_.group(settingsCodec(p_227640_), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((p_227656_) -> {
            return p_227656_.startPool;
        }), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((p_227654_) -> {
            return p_227654_.startJigsawName;
        }), Codec.intRange(0, 7).fieldOf("size").forGetter((p_227652_) -> {
            return p_227652_.maxDepth;
        }), Codec.BOOL.fieldOf("use_expansion_hack").forGetter((p_227646_) -> {
            return p_227646_.useExpansionHack;
        }), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((p_227644_) -> {
            return p_227644_.projectStartToHeightmap;
        }), Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter((p_227642_) -> {
            return p_227642_.maxDistanceFromCenter;
        }), Codec.intRange(0, 320).optionalFieldOf("min_height").forGetter((p_227654_) -> {
            return p_227654_.minHeight;
        })).apply(p_227640_, ShatterRealmStructure::new);
    }), ShatterRealmStructure::verifyRange).codec();
    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final Optional<Integer> minHeight;

    private static DataResult<ShatterRealmStructure> verifyRange(ShatterRealmStructure p_286886_) {
        byte b0;
        switch (p_286886_.terrainAdaptation()) {
            case NONE:
                b0 = 0;
                break;
            case BURY:
            case BEARD_THIN:
            case BEARD_BOX:
                b0 = 12;
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        int i = b0;
        return p_286886_.maxDistanceFromCenter + i > 128 ? DataResult.error(() -> {
            return "Structure size including terrain adaptation must not exceed 128";
        }) : DataResult.success(p_286886_);
    }

    public ShatterRealmStructure(Structure.StructureSettings p_227627_, Holder<StructureTemplatePool> p_227628_, Optional<ResourceLocation> p_227629_, int p_227630_, boolean p_227632_, Optional<Heightmap.Types> p_227633_, int p_227634_, Optional<Integer> minHeight) {
        super(p_227627_);
        this.startPool = p_227628_;
        this.startJigsawName = p_227629_;
        this.maxDepth = p_227630_;
        this.useExpansionHack = p_227632_;
        this.projectStartToHeightmap = p_227633_;
        this.maxDistanceFromCenter = p_227634_;
        this.minHeight = minHeight;
    }

    public ShatterRealmStructure(Structure.StructureSettings pSettings, Holder<StructureTemplatePool> pStartPool, int pMaxDepth, boolean pUseExpansionHack, Heightmap.Types pProjectStartToHeightmap) {
        this(pSettings, pStartPool, Optional.empty(), pMaxDepth, pUseExpansionHack, Optional.of(pProjectStartToHeightmap), 80, Optional.of(Integer.valueOf(0)));
    }

    public ShatterRealmStructure(Structure.StructureSettings pSettings, Holder<StructureTemplatePool> pStartPool, int pMaxDepth, boolean pUseExpansionHack) {
        this(pSettings, pStartPool, Optional.empty(), pMaxDepth, pUseExpansionHack, Optional.empty(), 80, Optional.of(Integer.valueOf(0)));
    }

    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
        Rotation rotation = Rotation.getRandom(pContext.random());
        BlockPos blockpos = this.getLowestYIn5by5BoxOffset7Blocks(pContext, rotation);
        if (!minHeight.isPresent() || blockpos.getY() >= minHeight.get()) {
            return JigsawPlacement.addPieces(pContext, this.startPool, this.startJigsawName, this.maxDepth, blockpos, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter);
        }
        return Optional.empty();
    }

    public StructureType<?> type() {
        return StructureInit.SHATTERREALMSTRUCTURE.get();
    }
}
