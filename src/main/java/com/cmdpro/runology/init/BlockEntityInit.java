package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.entity.RunicWorkbenchBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Runology.MOD_ID);
    public static final RegistryObject<BlockEntityType<RunicWorkbenchBlockEntity>> RUNICWORKBENCH =
            BLOCK_ENTITIES.register("runicworkbench_block_entity", () ->
                    BlockEntityType.Builder.of(RunicWorkbenchBlockEntity::new,
                            BlockInit.RUNICWORKBENCH.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
