package com.cmdpro.runology.datagen.loot;

import com.cmdpro.runology.registry.BlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }
    @Override
    protected void generate() {
        dropSelf(BlockRegistry.SHATTERSTONE.get());
        dropSelf(BlockRegistry.SHATTERED_FOCUS.get());
        dropSelf(BlockRegistry.SHATTERED_RELAY.get());
        dropSelf(BlockRegistry.RUNE_HEAT_SHATTERSTONE.get());
        dropSelf(BlockRegistry.RUNE_SHAPE_SHATTERSTONE.get());
        dropSelf(BlockRegistry.RUNE_FROST_SHATTERSTONE.get());
        dropSelf(BlockRegistry.RUNE_MOTION_SHATTERSTONE.get());
    }
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map((block) -> (Block)block.get()).filter((block) -> {
            if (block instanceof LiquidBlock) {
                return false;
            }
            return true;
        })::iterator;
    }
}