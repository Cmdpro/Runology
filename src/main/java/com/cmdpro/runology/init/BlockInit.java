package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.*;
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
    public static final RegistryObject<Block> EARTHORE = register("deepslateearthore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> AIRORE = register("deepslateairore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> WATERORE = register("deepslatewaterore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> FIREORE = register("deepslatefireore",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_LAPIS_ORE)), object -> () -> new BlockItem(object.get(), new Item.Properties()));
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
