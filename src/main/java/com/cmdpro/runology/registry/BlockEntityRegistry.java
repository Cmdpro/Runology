package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.world.ShatterBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Runology.MODID);
    public static final Supplier<BlockEntityType<ShatterBlockEntity>> SHATTER =
            register("shatter", () ->
                    BlockEntityType.Builder.of(ShatterBlockEntity::new,
                            BlockRegistry.SHATTER.get()).build(null));


    private static <T extends BlockEntityType<?>> Supplier<T> register(final String name, final Supplier<T> blockentity) {
        return BLOCK_ENTITIES.register(name, blockentity);
    }
}
