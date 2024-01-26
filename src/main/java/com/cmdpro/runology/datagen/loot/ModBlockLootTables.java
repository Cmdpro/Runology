package com.cmdpro.runology.datagen.loot;

import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.ItemInit;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(BlockInit.AIRORE.get(),
                block -> createElementalOreDrops(BlockInit.AIRORE.get(), ItemInit.AIRPOWDER.get()));
        this.add(BlockInit.EARTHORE.get(),
                block -> createElementalOreDrops(BlockInit.EARTHORE.get(), ItemInit.EARTHPOWDER.get()));
        this.add(BlockInit.WATERORE.get(),
                block -> createElementalOreDrops(BlockInit.WATERORE.get(), ItemInit.WATERPOWDER.get()));
        this.add(BlockInit.FIREORE.get(),
                block -> createElementalOreDrops(BlockInit.FIREORE.get(), ItemInit.FIREPOWDER.get()));
        this.add(BlockInit.MYSTERIUMORE.get(),
                block -> createElementalOreDrops(BlockInit.MYSTERIUMORE.get(), ItemInit.RAWMYSTERIUM.get()));
        dropSelf(BlockInit.CHISELEDSHATTERSTONEBRICKS.get());
        dropSelf(BlockInit.CRACKEDSHATTERSTONEBRICKS.get());
        dropSelf(BlockInit.MYSTERIUMBLOCK.get());
        dropSelf(BlockInit.RAWMYSTERIUMBLOCK.get());
        dropSelf(BlockInit.RUNICWORKBENCH.get());
        dropSelf(BlockInit.SHATTERCRYSTAL.get());
        dropSelf(BlockInit.SHATTERLEAF.get());
        dropSelf(BlockInit.SHATTERSTONE.get());
        dropSelf(BlockInit.SHATTERSTONEBRICKS.get());
        dropSelf(BlockInit.SHATTERSTONEBRICKSLAB.get());
        dropSelf(BlockInit.SHATTERSTONEBRICKSTAIRS.get());
        dropSelf(BlockInit.SHATTERSTONEBRICKWALL.get());
        dropSelf(BlockInit.SHATTERSTONEPILLAR.get());
        dropSelf(BlockInit.SHATTERSTONESLAB.get());
        dropSelf(BlockInit.SHATTERSTONESTAIRS.get());
        dropSelf(BlockInit.SHATTERSTONEWALL.get());
        dropSelf(BlockInit.VOIDGLASS.get());
        dropSelf(BlockInit.RUNICANALYZER.get());
        this.add(BlockInit.MYSTERIOUSALTAR.get(),
                block -> noDrop());
        this.add(BlockInit.POTTED_SHATTERLEAF.get(), block -> {
            return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.FLOWER_POT).when(ExplosionCondition.survivesExplosion()))).withPool(LootPool.lootPool().add(LootItem.lootTableItem(BlockInit.SHATTERLEAF.get()).when(ExplosionCondition.survivesExplosion())));
        });
        dropWhenSilkTouch(BlockInit.PETRIFIEDSHATTERWOOD.get());
    }

    protected LootTable.Builder createMysteriumOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }
    protected LootTable.Builder createElementalOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}