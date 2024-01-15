package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.*;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            Runology.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = ItemInit.ITEMS;
    public static final RegistryObject<Block> RUNICWORKBENCH = registerBlock("runicworkbench",
            () -> new RunicWorkbench(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion().strength(2.0f)));
    public static final RegistryObject<Block> SHATTERSTONE = register("shatterstone",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHATTERSTONEBRICKS = register("shatterstonebricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHATTERSTONEPILLAR = register("shatterstonepillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> CRACKEDSHATTERSTONEBRICKS = register("crackedshatterstonebricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.CRACKED_DEEPSLATE_BRICKS)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> CHISELEDSHATTERSTONEBRICKS = register("chiseledshatterstonebricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.CHISELED_DEEPSLATE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHATTERSTONEWALL = register("shatterstonewall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.COBBLED_DEEPSLATE_WALL)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHATTERSTONESLAB = register("shatterstoneslab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.COBBLED_DEEPSLATE_SLAB)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHATTERSTONESTAIRS = register("shatterstonestairs",
            () -> new StairBlock(SHATTERSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.COBBLED_DEEPSLATE_STAIRS)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHATTERSTONEBRICKWALL = register("shatterstonebrickwall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICK_WALL)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHATTERSTONEBRICKSLAB = register("shatterstonebrickslab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICK_SLAB)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHATTERSTONEBRICKSTAIRS = register("shatterstonebrickstairs",
            () -> new StairBlock(SHATTERSTONEBRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICK_STAIRS)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> EARTHORE = register("deepslateearthore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> AIRORE = register("deepslateairore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> WATERORE = register("deepslatewaterore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> FIREORE = register("deepslatefireore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> MYSTERIUMORE = register("mysteriumore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE).lightLevel((state) -> 5)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> MYSTERIUMBLOCK = register("mysteriumblock",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).lightLevel((state) -> 10)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SHATTERLEAF = register("shatterleaf",
            () -> new Shatterleaf(() -> MobEffects.BLINDNESS, 5,
                    BlockBehaviour.Properties.copy(Blocks.ALLIUM).noOcclusion().noCollission().lightLevel(Shatterleaf.LIGHT_EMISSION)), (object) -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> POTTED_SHATTERLEAF = BLOCKS.register("pottedshatterleaf",
            () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), BlockInit.SHATTERLEAF,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_ALLIUM).noOcclusion()));
    private static <T extends Block> RegistryObject<T> registerBlock(final String name,
                                                                     final Supplier<? extends T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(final String name, final Supplier<? extends T> block,
                                                                Function<RegistryObject<T>, Supplier<? extends Item>> item) {
        RegistryObject<T> obj = registerBlock(name, block);
        ITEMS.register(name, item.apply(obj));
        return obj;
    }
}
