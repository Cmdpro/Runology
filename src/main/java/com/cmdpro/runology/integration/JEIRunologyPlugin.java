package com.cmdpro.runology.integration;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.init.RecipeInit;
import com.cmdpro.runology.recipe.IRunicRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.*;

@JeiPlugin
public class JEIRunologyPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Runology.MOD_ID, "jei_plugin");
    }

    public static IJeiRuntime runTime;
    public static final RecipeType RUNICRECIPE = RecipeType.create(Runology.MOD_ID, RecipeInit.RUNICCRAFTING.getId().getPath(), IRunicRecipe.class);
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RunicCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<IRunicRecipe> recipes = rm.getAllRecipesFor(RecipeInit.RUNICCRAFTING.get());
        registration.addRecipes(RUNICRECIPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ItemInit.RUNICWORKBENCHITEM.get()), RUNICRECIPE);
    }
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

        runTime = jeiRuntime;

    }
}
