package com.cmdpro.runology.datagen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.BlockInit;
import com.cmdpro.runology.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
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
        simpleItem(ItemInit.SHATTEREDSOUL);
        simpleItem(ItemInit.PURITYARROW);
        simpleItem(ItemInit.PURITYPOWDER);
        simpleItem(ItemInit.PURITYRUNE);
        simpleItem(ItemInit.SUMMONTOTEMSPELLCRYSTAL);
        simpleItem(ItemInit.DIMENSIONALTHREATMUSICDISC);
        simpleItem(ItemInit.ECHOGOGGLES);
        simpleItem(ItemInit.ICESHARDSSPELLCRYSTAL);
        simpleItem(ItemInit.FIREBALLSPELLCRYSTAL);
        simpleItem(ItemInit.INSTABILITYRUNE);
        simpleItem(ItemInit.INSTABILITYPOWDER);
        simpleItem(ItemInit.PURIFIEDFLESH);
        simpleItem(ItemInit.ENHANCEDBONEMEAL);
        simpleItem(ItemInit.DRAGONIUMINGOT);
        simpleItem(ItemInit.LANTERNOFFLAMES);
        simpleItem(ItemInit.RUNICWASTE);
        simpleItem(ItemInit.TRANSMUTATIVESOLUTIONBUCKET);
        simpleItem(ItemInit.LIQUIDSOULSBUCKET);
        simpleItem(ItemInit.CONJURESPARKSPELLCRYSTAL);
        simpleItem(ItemInit.PRISMATICSHARD);
        simpleItem(ItemInit.PRISMATICINGOT);
        simpleItem(ItemInit.BLANKUPGRADE);
        simpleItem(ItemInit.WATERUPGRADE);
        simpleItem(ItemInit.EARTHUPGRADE);
        simpleItem(ItemInit.FIREUPGRADE);
        simpleItem(ItemInit.AIRUPGRADE);
        simpleItem(ItemInit.TRANSFORMATIONUPGRADE);


        handheldItem(ItemInit.REALITYSLICER);
        handheldItem(ItemInit.REALITYBREAKER);
        handheldItem(ItemInit.ANCIENTDRAGONSBLADE);

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
        evenSimplerBlockItem(BlockInit.SPELLTABLE);
        evenSimplerBlockItem(BlockInit.RUNICCAULDRON);
        evenSimplerBlockItem(BlockInit.ENDERTRANSPORTER);
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
        instabilityResonator(ItemInit.INSTABILITYRESONATOR);
        research(ItemInit.RESEARCH);
        flatBlockItemWithTexture(BlockInit.SHATTERLEAF, new ResourceLocation(Runology.MOD_ID, "block/shatterleafempty"));
        tornResearch(ItemInit.ANCIENTDRAGONSBLADETORNRESEARCH);
        tornResearch(ItemInit.ECHOGOGGLESTORNRESEARCH);
        tornResearch(ItemInit.LANTERNOFFLAMESTORNRESEARCH);
        prismaticBlaster(ItemInit.PRISMATICBLASTER);
        elytra(ItemInit.DRAGONIUMELYTRA, new ResourceLocation(Runology.MOD_ID, "item/brokendragoniumelytra"));
        simpleItem(ItemInit.PRISMATICELYTRA);
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Runology.MOD_ID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder tornResearch(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Runology.MOD_ID,"item/tornresearch"));
    }
    private ItemModelBuilder flatBlockItemWithTexture(RegistryObject<Block> item, ResourceLocation texture) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                texture);
    }
    private ItemModelBuilder instabilityResonator(RegistryObject<Item> item) {
        withExistingParent("instabilityreader2", new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/instabilityreader2"));
        withExistingParent("instabilityreader3", new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/instabilityreader3"));
        withExistingParent("instabilityreader4", new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/instabilityreader4"));
        withExistingParent("instabilityreader5", new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/instabilityreader5"));
        withExistingParent("instabilityreader6", new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/instabilityreader6"));
        withExistingParent("instabilityreader7", new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/instabilityreader7"));
        withExistingParent("instabilityreader8", new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/instabilityreader8"));
        withExistingParent("instabilityreader9", new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/instabilityreader9"));
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                        new ResourceLocation(Runology.MOD_ID,"item/instabilityreader1"))
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "instability"), 0).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "instabilityreader"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "instability"), 125).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "instabilityreader2"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "instability"), 250).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "instabilityreader3"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "instability"), 375).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "instabilityreader4"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "instability"), 500).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "instabilityreader5"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "instability"), 625).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "instabilityreader6"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "instability"), 750).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "instabilityreader7"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "instability"), 875).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "instabilityreader8"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "instability"), 1000).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "instabilityreader9"))).end();
    }
    private ItemModelBuilder elytra(RegistryObject<Item> item, ResourceLocation broken) {
        withExistingParent("broken" + item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", broken);
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                        new ResourceLocation(Runology.MOD_ID, "item/" + item.getId().getPath()))
                .override().predicate(new ResourceLocation("broken"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "item/broken" + item.getId().getPath()))).end();
    }
    public ItemModelBuilder prismaticBlasterPart(String name) {
        return getBuilder(name).parent(getExistingFile(new ResourceLocation(Runology.MOD_ID, "item/reversehandheld")));
    }
    private ItemModelBuilder prismaticBlaster(RegistryObject<Item> item) {
        prismaticBlasterPart("prismaticblaster/charging2").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/charging2"));
        prismaticBlasterPart("prismaticblaster/charging3").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/charging3"));
        prismaticBlasterPart("prismaticblaster/charging4").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/charging4"));
        prismaticBlasterPart("prismaticblaster/charging5").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/charging5"));
        prismaticBlasterPart("prismaticblaster/charging6").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/charging6"));
        prismaticBlasterPart("prismaticblaster/charging7").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/charging7"));
        prismaticBlasterPart("prismaticblaster/charged").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/charged"));
        prismaticBlasterPart("prismaticblaster/transcharging1").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/transcharging1"));
        prismaticBlasterPart("prismaticblaster/transcharging2").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/transcharging2"));
        prismaticBlasterPart("prismaticblaster/transcharging3").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/transcharging3"));
        prismaticBlasterPart("prismaticblaster/transcharging4").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/transcharging4"));
        prismaticBlasterPart("prismaticblaster/transcharging5").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/transcharging5"));
        prismaticBlasterPart("prismaticblaster/transcharging6").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/transcharging6"));
        prismaticBlasterPart("prismaticblaster/transcharging7").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/transcharging7"));
        prismaticBlasterPart("prismaticblaster/transcharged").texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/transcharged"));
        return prismaticBlasterPart(item.getId().getPath()).texture("layer0",
                        new ResourceLocation(Runology.MOD_ID,"item/prismaticblaster/charging1"))
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 0).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/charging2"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 2).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/charging3"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 3).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/charging4"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 4).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/charging5"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 5).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/charging6"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 6).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/charging7"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 7).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/charged"))).end()

                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 0).predicate(new ResourceLocation(Runology.MOD_ID, "blastertype"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/transcharging1"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 1).predicate(new ResourceLocation(Runology.MOD_ID, "blastertype"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/transcharging2"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 2).predicate(new ResourceLocation(Runology.MOD_ID, "blastertype"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/transcharging3"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 3).predicate(new ResourceLocation(Runology.MOD_ID, "blastertype"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/transcharging4"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 4).predicate(new ResourceLocation(Runology.MOD_ID, "blastertype"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/transcharging5"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 5).predicate(new ResourceLocation(Runology.MOD_ID, "blastertype"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/transcharging6"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 6).predicate(new ResourceLocation(Runology.MOD_ID, "blastertype"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/transcharging7"))).end()
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "charge"), 7).predicate(new ResourceLocation(Runology.MOD_ID, "blastertype"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "prismaticblaster/transcharged"))).end();
    }
    private ItemModelBuilder research(RegistryObject<Item> item) {
        withExistingParent("writtenresearch", new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(Runology.MOD_ID,"item/writtenresearch"));
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                        new ResourceLocation(Runology.MOD_ID,"item/unwrittenresearch"))
                .override().predicate(new ResourceLocation(Runology.MOD_ID, "finished"), 1).
                model(getExistingFile(new ResourceLocation(Runology.MOD_ID, "writtenresearch"))).end();
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
