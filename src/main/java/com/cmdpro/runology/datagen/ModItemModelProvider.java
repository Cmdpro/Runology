package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.registry.BlockRegistry;
import com.cmdpro.runology.registry.ItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Runology.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ItemRegistry.GUIDEBOOK);
        flatBlockItemWithTexture(BlockRegistry.SHATTER, Runology.locate("item/shatter"));
        simpleItem(ItemRegistry.GOLD_CHISEL);
        simpleItem(ItemRegistry.RUNIC_CHISEL);
        evenSimplerBlockItem(BlockRegistry.SHATTERED_FOCUS);
        evenSimplerBlockItem(BlockRegistry.SHATTERED_RELAY);
        evenSimplerBlockItem(BlockRegistry.GOLD_PILLAR);
        evenSimplerBlockItem(BlockRegistry.SHATTERED_INFUSER);
        evenSimplerBlockItem(BlockRegistry.SHATTER_COIL);
        evenSimplerBlockItem(BlockRegistry.HEAT_FOCUS);
        evenSimplerBlockItem(BlockRegistry.REALITY_FOCUS);
        simpleItem(ItemRegistry.SHATTERED_FLOW_ICON);
        simpleItem(ItemRegistry.SHATTER_READER);
        evenSimplerBlockItem(BlockRegistry.SHATTERSTONE_BRICK_STAIRS);
        evenSimplerBlockItem(BlockRegistry.SHATTERSTONE_BRICK_SLAB);
        wallItem(BlockRegistry.SHATTERSTONE_BRICK_WALL, BlockRegistry.SHATTERSTONE_BRICKS);
        simpleItem(ItemRegistry.SHATTERED_SHARD);
        simpleItem(ItemRegistry.BLINK_BOOTS);
        simpleItem(ItemRegistry.SHATTERED_INGOT);
    }
    private ItemModelBuilder simpleItem(Supplier<Item> item) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Runology.MODID,"item/" + BuiltInRegistries.ITEM.getKey(item.get()).getPath()));
    }
    private ItemModelBuilder simpleItemWithSubdirectory(Supplier<Item> item, String subdirectory) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Runology.MODID,"item/" + subdirectory + "/" + BuiltInRegistries.ITEM.getKey(item.get()).getPath()));
    }
    private ItemModelBuilder flatBlockItemWithTexture(Supplier<Block> item, ResourceLocation texture) {
        return withExistingParent(BuiltInRegistries.BLOCK.getKey(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                texture);
    }

    public void evenSimplerBlockItem(Supplier<Block> block) {
        this.withExistingParent(Runology.MODID + ":" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath(),
                modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath()));
    }
    public void wallItem(Supplier<Block> block, Supplier<Block> baseBlock) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  Runology.locate("block/" + BuiltInRegistries.BLOCK.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder handheldItem(Supplier<Item> item) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/handheld")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Runology.MODID,"item/" + BuiltInRegistries.ITEM.getKey(item.get()).getPath()));
    }

    private ItemModelBuilder simpleBlockItem(Supplier<Block> item) {
        return withExistingParent(BuiltInRegistries.BLOCK.getKey(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Runology.MODID,"item/" + BuiltInRegistries.BLOCK.getKey(item.get()).getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(Supplier<Block> item) {
        return withExistingParent(BuiltInRegistries.BLOCK.getKey(item.get()).getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Runology.MODID,"block/" + BuiltInRegistries.BLOCK.getKey(item.get()).getPath()));
    }
}
