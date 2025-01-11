package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterialRegistry {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL,
            Runology.MODID);
    public static final Holder<ArmorMaterial> BLINK = ARMOR_MATERIALS.register("blink", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.BOOTS, 1);
            }),
            25,
            SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(Tags.Items.INGOTS_GOLD), List.of(
                    new ArmorMaterial.Layer(
                            Runology.locate("blink")
                    )
            ),
            0,
            0
    ));

    private static <T extends ArmorMaterial> DeferredHolder<ArmorMaterial, T> register(final String name, final Supplier<T> material) {
        return ARMOR_MATERIALS.register(name, material);
    }
}
