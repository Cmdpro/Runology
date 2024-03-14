package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.entity.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Runology.MOD_ID);
    public static final RegistryObject<BlockEntityType<RunicWorkbenchBlockEntity>> RUNICWORKBENCH =
            BLOCK_ENTITIES.register("runicworkbenchblockentity", () ->
                    BlockEntityType.Builder.of(RunicWorkbenchBlockEntity::new,
                            BlockInit.RUNICWORKBENCH.get()).build(null));
    public static final RegistryObject<BlockEntityType<RunicCauldronBlockEntity>> RUNICCAULDRON =
            BLOCK_ENTITIES.register("runiccauldronblockentity", () ->
                    BlockEntityType.Builder.of(RunicCauldronBlockEntity::new,
                            BlockInit.RUNICCAULDRON.get()).build(null));
    public static final RegistryObject<BlockEntityType<SpellTableBlockEntity>> SPELLTABLE =
            BLOCK_ENTITIES.register("spelltableblockentity", () ->
                    BlockEntityType.Builder.of(SpellTableBlockEntity::new,
                            BlockInit.SPELLTABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<RunicAnalyzerBlockEntity>> RUNICANALYZER =
            BLOCK_ENTITIES.register("runicanalyzerblockentity", () ->
                    BlockEntityType.Builder.of(RunicAnalyzerBlockEntity::new,
                            BlockInit.RUNICANALYZER.get()).build(null));
    public static final RegistryObject<BlockEntityType<VoidGlassBlockEntity>> VOIDGLASS =
            BLOCK_ENTITIES.register("voidglassblockentity", () ->
                    BlockEntityType.Builder.of(VoidGlassBlockEntity::new,
                            BlockInit.VOIDGLASS.get()).build(null));
    public static final RegistryObject<BlockEntityType<EnderTransporterBlockEntity>> ENDERTRANSPORTER =
            BLOCK_ENTITIES.register("endertransporterblockentity", () ->
                    BlockEntityType.Builder.of(EnderTransporterBlockEntity::new,
                            BlockInit.ENDERTRANSPORTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SparkBlockEntity>> SPARK =
            BLOCK_ENTITIES.register("sparkblockentity", () ->
                    BlockEntityType.Builder.of(SparkBlockEntity::new,
                            BlockInit.SPARK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
