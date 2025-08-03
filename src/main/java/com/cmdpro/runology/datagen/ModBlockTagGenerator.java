package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.registry.BlockRegistry;
import com.cmdpro.runology.registry.TagRegistry;
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
                .add(BlockRegistry.SHATTERED_RELAY.get())
                .add(BlockRegistry.RUNE_HEAT_SHATTERSTONE.get())
                .add(BlockRegistry.RUNE_SHAPE_SHATTERSTONE.get())
                .add(BlockRegistry.RUNE_FROST_SHATTERSTONE.get())
                .add(BlockRegistry.RUNE_MOTION_SHATTERSTONE.get())
                .add(BlockRegistry.GOLD_PILLAR.get())
                .add(BlockRegistry.SHATTERED_INFUSER.get())
                .add(BlockRegistry.SHATTER_COIL.get())
                .add(BlockRegistry.SHATTER_COIL_FILLER.get())
                .add(BlockRegistry.HEAT_FOCUS.get())
                .add(BlockRegistry.SHATTERED_BLOCK.get())
                .add(BlockRegistry.OTHERWORLDLY_STONE.get())
                .add(BlockRegistry.OTHERWORLDLY_SANDSTONE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(BlockRegistry.SHATTERSTONE.get())
                .add(BlockRegistry.SHATTERSTONE_BRICKS.get())
                .add(BlockRegistry.SHATTERSTONE_BRICK_STAIRS.get())
                .add(BlockRegistry.SHATTERSTONE_BRICK_SLAB.get())
                .add(BlockRegistry.SHATTERSTONE_BRICK_WALL.get());
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(BlockRegistry.SHATTERED_BLOCK.get());
        this.tag(BlockTags.WALLS)
                .add(BlockRegistry.SHATTERSTONE_BRICK_WALL.get());
        this.tag(BlockTags.DIRT)
                .add(BlockRegistry.OTHERWORLDLY_GRASS_BLOCK.get())
                .add(BlockRegistry.OTHERWORLDLY_DIRT.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(BlockRegistry.OTHERWORLDLY_GRASS_BLOCK.get())
                .add(BlockRegistry.OTHERWORLDLY_DIRT.get())
                .add(BlockRegistry.OTHERWORLDLY_SAND.get());
        this.tag(BlockTags.SAND)
                .add(BlockRegistry.OTHERWORLDLY_SAND.get());
    }
}
