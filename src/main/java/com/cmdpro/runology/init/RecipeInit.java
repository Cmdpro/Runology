package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.recipe.IRunicRecipe;
import com.cmdpro.runology.recipe.ShapedRunicRecipe;
import com.cmdpro.runology.recipe.ShapelessRunicRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Runology.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Runology.MOD_ID);

    public static final RegistryObject<RecipeSerializer<ShapelessRunicRecipe>> SHAPELESSRUNICRECIPE = RECIPES.register("shapelessrunicrecipe", ShapelessRunicRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<ShapedRunicRecipe>> SHAPEDRUNICRECIPE = RECIPES.register("shapedrunicrecipe", ShapedRunicRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<IRunicRecipe>> RUNICCRAFTING =
            RECIPE_TYPES.register("runicrecipe", () -> RecipeType.simple(new ResourceLocation(Runology.MOD_ID, "runicrecipe")));
    public static void register(IEventBus eventBus) {
        RECIPES.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
