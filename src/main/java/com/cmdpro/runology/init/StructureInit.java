package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.worldgen.structures.ShatterRealmStructure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class StructureInit {
    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE,
            Runology.MOD_ID);

    public static final RegistryObject<StructureType<ShatterRealmStructure>> SHATTERREALMSTRUCTURE = STRUCTURES.register("shatterrealmstructure", () -> () -> ShatterRealmStructure.CODEC);

    public static void register(IEventBus eventBus) {
        STRUCTURES.register(eventBus);
    }
}
