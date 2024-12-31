package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Runology.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockRegistry.SHATTERSTONE.get())
                .add(BlockRegistry.SHATTERSTONE_BRICKS.get())
                .add(BlockRegistry.SHATTERSTONE_BRICK_STAIRS.get())
                .add(BlockRegistry.SHATTERSTONE_BRICK_SLAB.get())
                .add(BlockRegistry.SHATTERSTONE_BRICK_WALL.get())
                .add(BlockRegistry.SHATTERED_FOCUS.get())
                .add(BlockRegistry.RUNE_HEAT_SHATTERSTONE.get())
                .add(BlockRegistry.RUNE_SHAPE_SHATTERSTONE.get())
                .add(BlockRegistry.RUNE_FROST_SHATTERSTONE.get())
                .add(BlockRegistry.RUNE_MOTION_SHATTERSTONE.get())
                .add(BlockRegistry.GOLD_PILLAR.get())
                .add(BlockRegistry.SHATTERED_INFUSER.get())
                .add(BlockRegistry.SHATTERED_SHARD_ORE.get())
                .add(BlockRegistry.DEEPSLATE_SHATTERED_SHARD_ORE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(BlockRegistry.SHATTERSTONE.get())
                .add(BlockRegistry.SHATTERSTONE_BRICKS.get())
                .add(BlockRegistry.SHATTERSTONE_BRICK_STAIRS.get())
                .add(BlockRegistry.SHATTERSTONE_BRICK_SLAB.get())
                .add(BlockRegistry.SHATTERSTONE_BRICK_WALL.get())
                .add(BlockRegistry.SHATTERED_SHARD_ORE.get())
                .add(BlockRegistry.DEEPSLATE_SHATTERED_SHARD_ORE.get());
        this.tag(BlockTags.WALLS)
                .add(BlockRegistry.SHATTERSTONE_BRICK_WALL.get());
    }
}
