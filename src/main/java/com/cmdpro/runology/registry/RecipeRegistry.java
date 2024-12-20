package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.recipe.ShatterImbuementRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Runology.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Runology.MODID);
    public static final Supplier<RecipeSerializer<ShatterImbuementRecipe>> SHATTER_IMBUEMENT =
            registerSerializer("shatter_imbuement", () -> ShatterImbuementRecipe.Serializer.INSTANCE);

    public static final Supplier<RecipeType<ShatterImbuementRecipe>> SHATTER_IMBUEMENT_TYPE =
            registerBasicRecipeType("shatter_imbuement");
    private static <T extends RecipeType<?>> Supplier<T> registerType(final String name, final Supplier<T> recipe) {
        return RECIPE_TYPES.register(name, recipe);
    }
    static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> registerBasicRecipeType(final String id) {
        return RECIPE_TYPES.register(id, () -> new RecipeType<T>() {
            public String toString() {
                return id;
            }
        });
    }
    private static <T extends RecipeSerializer<?>> Supplier<T> registerSerializer(final String name, final Supplier<T> recipe) {
        return RECIPES.register(name, recipe);
    }
}
