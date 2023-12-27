package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.block.entity.DivinationTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RunicArts.MOD_ID);

    public static final RegistryObject<BlockEntityType<SpiritTankBlockEntity>> SPIRITTANK =
            BLOCK_ENTITIES.register("spirittank_block_entity", () ->
                    BlockEntityType.Builder.of(SpiritTankBlockEntity::new,
                            BlockInit.SPIRITTANK.get()).build(null));
    public static final RegistryObject<BlockEntityType<SoulPointBlockEntity>> SOULPOINT =
            BLOCK_ENTITIES.register("soulpoint_block_entity", () ->
                    BlockEntityType.Builder.of(SoulPointBlockEntity::new,
                            BlockInit.SOULPOINT.get()).build(null));
    public static final RegistryObject<BlockEntityType<SoulAltarBlockEntity>> SOULALTAR =
            BLOCK_ENTITIES.register("soulaltar_block_entity", () ->
                    BlockEntityType.Builder.of(SoulAltarBlockEntity::new,
                            BlockInit.SOULALTAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<DivinationTableBlockEntity>> DIVINATIONTABLE =
            BLOCK_ENTITIES.register("divinationtable_block_entity", () ->
                    BlockEntityType.Builder.of(DivinationTableBlockEntity::new,
                            BlockInit.DIVINATIONTABLE.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
