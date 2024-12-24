package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.machines.ShatteredInfuserBlockEntity;
import com.cmdpro.runology.block.misc.GoldPillarBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
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
    public static final Supplier<BlockEntityType<ShatteredFocusBlockEntity>> SHATTERED_FOCUS =
            register("shattered_focus", () ->
                    BlockEntityType.Builder.of(ShatteredFocusBlockEntity::new,
                            BlockRegistry.SHATTERED_FOCUS.get()).build(null));
    public static final Supplier<BlockEntityType<ShatteredRelayBlockEntity>> SHATTERED_RELAY =
            register("shattered_relay", () ->
                    BlockEntityType.Builder.of(ShatteredRelayBlockEntity::new,
                            BlockRegistry.SHATTERED_RELAY.get()).build(null));
    public static final Supplier<BlockEntityType<GoldPillarBlockEntity>> GOLD_PILLAR =
            register("gold_pillar", () ->
                    BlockEntityType.Builder.of(GoldPillarBlockEntity::new,
                            BlockRegistry.GOLD_PILLAR.get()).build(null));
    public static final Supplier<BlockEntityType<ShatteredInfuserBlockEntity>> SHATTERED_INFUSER =
            register("shattered_infuser", () ->
                    BlockEntityType.Builder.of(ShatteredInfuserBlockEntity::new,
                            BlockRegistry.SHATTERED_INFUSER.get()).build(null));


    private static <T extends BlockEntityType<?>> Supplier<T> register(final String name, final Supplier<T> blockentity) {
        return BLOCK_ENTITIES.register(name, blockentity);
    }
}
