package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.machines.shattercoil.ShatterCoil;
import com.cmdpro.runology.block.machines.shattercoil.ShatterCoilBlockItem;
import com.cmdpro.runology.block.machines.shattercoil.ShatterCoilFiller;
import com.cmdpro.runology.block.machines.ShatteredInfuser;
import com.cmdpro.runology.block.misc.GoldPillar;
import com.cmdpro.runology.block.transmission.ShatteredFocus;
import com.cmdpro.runology.block.transmission.ShatteredRelay;
import com.cmdpro.runology.block.world.Shatter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK,
            Runology.MODID);
    public static final DeferredRegister<Item> ITEMS = ItemRegistry.ITEMS;

    public static final Supplier<Block> SHATTER = register("shatter",
            () -> new Shatter(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK).noOcclusion().noCollission().noTerrainParticles()),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTERED_FOCUS = register("shattered_focus",
            () -> new ShatteredFocus(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTERED_RELAY = register("shattered_relay",
            () -> new ShatteredRelay(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> GOLD_PILLAR = register("gold_pillar",
            () -> new GoldPillar(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTERED_INFUSER = register("shattered_infuser",
            () -> new ShatteredInfuser(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTER_COIL = register("shatter_coil",
            () -> new ShatterCoil(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()),
            object -> () -> new ShatterCoilBlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTER_COIL_FILLER = registerBlock("shatter_coil_filler",
            () -> new ShatterCoilFiller(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()));

    public static final Supplier<Block> SHATTERED_SHARD_ORE = register("shattered_shard_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> DEEPSLATE_SHATTERED_SHARD_ORE = register("deepslate_shattered_shard_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_DIAMOND_ORE)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTERSTONE = register("shatterstone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTERSTONE_BRICKS = register("shatterstone_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF_BRICKS)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTERSTONE_BRICK_STAIRS = register("shatterstone_brick_stairs",
            () -> new StairBlock(SHATTERSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF_BRICK_STAIRS)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTERSTONE_BRICK_SLAB = register("shatterstone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF_BRICK_SLAB)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHATTERSTONE_BRICK_WALL = register("shatterstone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF_BRICK_WALL)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> RUNE_HEAT_SHATTERSTONE = register("rune_heat_shatterstone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> RUNE_SHAPE_SHATTERSTONE = register("rune_shape_shatterstone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> RUNE_FROST_SHATTERSTONE = register("rune_frost_shatterstone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> RUNE_MOTION_SHATTERSTONE = register("rune_motion_shatterstone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TUFF)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));


    private static <T extends Block> Supplier<T> registerBlock(final String name,
                                                                     final Supplier<? extends T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> Supplier<T> register(final String name, final Supplier<? extends T> block,
                                                                Function<Supplier<T>, Supplier<? extends Item>> item) {
        Supplier<T> obj = registerBlock(name, block);
        ITEMS.register(name, item.apply(obj));
        return obj;
    }
    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }
    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }
}
