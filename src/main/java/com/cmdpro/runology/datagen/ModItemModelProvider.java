package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

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
        super(output, Runology.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ItemInit.AIRPOWDER);
        simpleItem(ItemInit.EARTHPOWDER);
        simpleItem(ItemInit.WATERPOWDER);
        simpleItem(ItemInit.FIREPOWDER);
        simpleItem(ItemInit.BLANKRUNE);
        simpleItem(ItemInit.AIRRUNE);
        simpleItem(ItemInit.WATERRUNE);
        simpleItem(ItemInit.EARTHRUNE);
        simpleItem(ItemInit.FIRERUNE);
        simpleItem(ItemInit.PLANTRUNE);
        simpleItem(ItemInit.VOIDRUNE);
        simpleItem(ItemInit.ENERGYRUNE);
        simpleItem(ItemInit.ICERUNE);
        simpleItem(ItemInit.MYSTERIUMCORE);
        simpleItem(ItemInit.MYSTERIUMINGOT);
        simpleItem(ItemInit.MYSTERIUMNUGGET);
        simpleItem(ItemInit.MYSTERIUMTOTEM);
        simpleItem(ItemInit.RAWMYSTERIUM);
        simpleItem(ItemInit.RUNOLOGYGUIDEICON);
        simpleItem(ItemInit.SHATTERBERRIES);

        withExistingParent(ItemInit.RUNICCONSTRUCTSPAWNEGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ItemInit.RUNICSCOUTSPAWNEGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        evenSimplerBlockItem(BlockInit.CHISELEDSHATTERSTONEBRICKS);
        evenSimplerBlockItem(BlockInit.CRACKEDSHATTERSTONEBRICKS);
        evenSimplerBlockItem(BlockInit.AIRORE);
        evenSimplerBlockItem(BlockInit.EARTHORE);
        evenSimplerBlockItem(BlockInit.WATERORE);
        evenSimplerBlockItem(BlockInit.FIREORE);
        evenSimplerBlockItem(BlockInit.MYSTERIOUSALTAR);
        evenSimplerBlockItem(BlockInit.MYSTERIUMBLOCK);
        evenSimplerBlockItem(BlockInit.MYSTERIUMORE);
        evenSimplerBlockItem(BlockInit.PETRIFIEDSHATTERWOOD);
        evenSimplerBlockItem(BlockInit.RAWMYSTERIUMBLOCK);
        evenSimplerBlockItem(BlockInit.RUNICANALYZER);
        evenSimplerBlockItem(BlockInit.RUNICWORKBENCH);
        evenSimplerBlockItem(BlockInit.SHATTERCRYSTAL);
        evenSimplerBlockItem(BlockInit.SHATTERSTONE);
        evenSimplerBlockItem(BlockInit.SHATTERSTONEBRICKS);
        evenSimplerBlockItem(BlockInit.SHATTERSTONEBRICKSLAB);
        evenSimplerBlockItem(BlockInit.SHATTERSTONEBRICKSTAIRS);
        evenSimplerBlockItem(BlockInit.SHATTERSTONEPILLAR);
        evenSimplerBlockItem(BlockInit.SHATTERSTONESLAB);
        evenSimplerBlockItem(BlockInit.SHATTERSTONESTAIRS);
        wallItem(BlockInit.SHATTERSTONEWALL, BlockInit.SHATTERSTONE);
        wallItem(BlockInit.SHATTERSTONEBRICKWALL, BlockInit.SHATTERSTONEBRICKS);
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Runology.MOD_ID,"item/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(Runology.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }
    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(Runology.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(Runology.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Runology.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Runology.MOD_ID,"block/" + item.getId().getPath()));
    }
}
