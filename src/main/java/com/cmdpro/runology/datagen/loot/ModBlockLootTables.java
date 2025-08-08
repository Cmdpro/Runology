package com.cmdpro.runology.datagen.loot;

import com.cmdpro.runology.registry.BlockRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }
    @Override
    protected void generate() {
        dropSelf(BlockRegistry.SHATTERSTONE.get());
        dropSelf(BlockRegistry.SHATTERSTONE_BRICKS.get());
        dropSelf(BlockRegistry.SHATTERSTONE_BRICK_SLAB.get());
        dropSelf(BlockRegistry.SHATTERSTONE_BRICK_WALL.get());
        dropSelf(BlockRegistry.SHATTERSTONE_BRICK_STAIRS.get());
        dropSelf(BlockRegistry.SHATTERED_FOCUS.get());
        dropSelf(BlockRegistry.SHATTERED_RELAY.get());
        dropSelf(BlockRegistry.RUNE_HEAT_SHATTERSTONE.get());
        dropSelf(BlockRegistry.RUNE_SHAPE_SHATTERSTONE.get());
        dropSelf(BlockRegistry.RUNE_FROST_SHATTERSTONE.get());
        dropSelf(BlockRegistry.RUNE_MOTION_SHATTERSTONE.get());
        dropSelf(BlockRegistry.GOLD_PILLAR.get());
        dropSelf(BlockRegistry.SHATTERED_INFUSER.get());
        dropSelf(BlockRegistry.SHATTER_COIL.get());
        add(BlockRegistry.SHATTER_COIL_FILLER.get(), (block) -> noDrop());
        dropSelf(BlockRegistry.HEAT_FOCUS.get());
        dropSelf(BlockRegistry.SHATTERED_BLOCK.get());
        dropSelf(BlockRegistry.OTHERWORLDLY_DIRT.get());
        dropSelf(BlockRegistry.OTHERWORLDLY_STONE.get());
        dropSelf(BlockRegistry.OTHERWORLDLY_STONE_BRICKS.get());
        dropSelf(BlockRegistry.OTHERWORLDLY_STONE_BRICK_SLAB.get());
        dropSelf(BlockRegistry.OTHERWORLDLY_STONE_BRICK_WALL.get());
        dropSelf(BlockRegistry.OTHERWORLDLY_STONE_BRICK_STAIRS.get());
        dropSelf(BlockRegistry.OTHERWORLDLY_SAND.get());
        dropSelf(BlockRegistry.OTHERWORLDLY_SANDSTONE.get());
        otherWhenSilkTouch(BlockRegistry.OTHERWORLDLY_GRASS_BLOCK.get(), BlockRegistry.OTHERWORLDLY_DIRT.get());
        this.add(BlockRegistry.TALL_OTHERWORLDLY_GRASS.get(),
                block -> createSpecialGrassDrops(BlockRegistry.TALL_OTHERWORLDLY_GRASS.get(), Items.AIR));
        this.add(BlockRegistry.SHORT_OTHERWORLDLY_GRASS.get(),
                block -> createSpecialGrassDrops(BlockRegistry.SHORT_OTHERWORLDLY_GRASS.get(), Items.AIR));
    }
    protected LootTable.Builder createSpecialGrassDrops(Block block, Item seeds) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createShearsDispatchTable(
                block,
                (LootPoolEntryContainer.Builder<?>)this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(seeds)
                                .when(LootItemRandomChanceCondition.randomChance(0.125F))
                                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE), 2))
                )
        );
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