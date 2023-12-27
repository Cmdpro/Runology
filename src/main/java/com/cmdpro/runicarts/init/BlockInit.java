package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            RunicArts.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = ItemInit.ITEMS;
    public static final RegistryObject<Block> SPIRITTANK = registerBlock("spirittank",
            () -> new SpiritTank(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion().strength(3.0f)));
    public static final RegistryObject<Block> DIVINATIONTABLE = registerBlock("divinationtable",
            () -> new DivinationTable(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.WOOL).noOcclusion().strength(3.0f)));
    public static final RegistryObject<Block> SOULPOINT = registerBlock("soulpoint",
            () -> new SoulPoint(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion().strength(3.0f)));
    public static final RegistryObject<Block> SOULALTAR = registerBlock("soulaltar",
            () -> new SoulAltar(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion().strength(3.0f)));
    public static final RegistryObject<Block> SOULSHAPER = register("soulshaper",
            () -> new SoulShaper(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(3.0f)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> SOULCASTERSTABLE = register("soulcasterstable",
            () -> new SoulcastersTable(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(3.0f).noOcclusion()),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
    public static final RegistryObject<Block> ECHOSOIL = register("echosoil",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SOUL_SOIL).mapColor(MapColor.COLOR_BLUE)),
            object -> () -> new BlockItem(object.get(), new Item.Properties()));
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
