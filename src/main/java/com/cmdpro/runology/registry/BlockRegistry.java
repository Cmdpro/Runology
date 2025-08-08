package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.machines.HeatFocus;
import com.cmdpro.runology.block.machines.shattercoil.ShatterCoil;
import com.cmdpro.runology.block.machines.shattercoil.ShatterCoilBlockItem;
import com.cmdpro.runology.block.machines.shattercoil.ShatterCoilFiller;
import com.cmdpro.runology.block.machines.ShatteredInfuser;
import com.cmdpro.runology.block.misc.GoldPillar;
import com.cmdpro.runology.block.transmission.ShatteredFocus;
import com.cmdpro.runology.block.transmission.ShatteredRelay;
import com.cmdpro.runology.block.world.OtherworldlyEnergy;
import com.cmdpro.runology.block.world.Shatter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ColorRGBA;
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
    public static final Supplier<Block> OTHERWORLDLY_ENERGY = register("otherworldly_energy",
            () -> new OtherworldlyEnergy(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK).noOcclusion().noCollission().noTerrainParticles()),
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
    public static final Supplier<Block> HEAT_FOCUS = register("heat_focus",
            () -> new HeatFocus(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).noOcclusion()),
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
    public static final Supplier<Block> SHATTERED_BLOCK = register("shattered_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> OTHERWORLDLY_DIRT = register("otherworldly_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> OTHERWORLDLY_GRASS_BLOCK = register("otherworldly_grass_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> OTHERWORLDLY_STONE = register("otherworldly_stone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> OTHERWORLDLY_STONE_BRICKS = register("otherworldly_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> OTHERWORLDLY_STONE_BRICK_STAIRS = register("otherworldly_stone_brick_stairs",
            () -> new StairBlock(OTHERWORLDLY_STONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_STAIRS)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> OTHERWORLDLY_STONE_BRICK_SLAB = register("otherworldly_stone_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_SLAB)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> OTHERWORLDLY_STONE_BRICK_WALL = register("otherworldly_stone_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_WALL)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> TALL_OTHERWORLDLY_GRASS = register("tall_otherworldly_grass",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TALL_GRASS).lightLevel((state) -> 2)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> SHORT_OTHERWORLDLY_GRASS = register("short_otherworldly_grass",
            () -> new TallGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS).lightLevel((state) -> 2)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> OTHERWORLDLY_SAND = register("otherworldly_sand",
            () -> new ColoredFallingBlock(new ColorRGBA(0xf2b6a4), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final Supplier<Block> OTHERWORLDLY_SANDSTONE = register("otherworldly_sandstone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)),
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
