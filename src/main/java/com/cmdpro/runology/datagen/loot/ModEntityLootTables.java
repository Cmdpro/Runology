package com.cmdpro.runology.datagen.loot;

import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.EntityInit;
import com.cmdpro.runology.init.ItemInit;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.text.spi.NumberFormatProvider;
import java.util.Set;
import java.util.stream.Stream;

public class ModEntityLootTables extends EntityLootSubProvider {
    public ModEntityLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }


    @Override
    public void generate() {
        add(EntityInit.RUNICCONSTRUCT.get(), LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(ItemInit.MYSTERIUMCORE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))));
        add(EntityInit.RUNICSCOUT.get(), LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(ItemInit.MYSTERIUMCORE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))));
        add(EntityInit.RUNICOVERSEER.get(), LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(ItemInit.DIMENSIONALTHREATMUSICDISC.get()).when(LootItemRandomChanceCondition.randomChance(0.25f))).add(LootItem.lootTableItem(ItemInit.SHATTEREDSOUL.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3))))));
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return EntityInit.ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
    }
}