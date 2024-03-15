package com.cmdpro.runology.integration.jei;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.init.RecipeInit;
import com.cmdpro.runology.recipe.IRunicRecipe;
import com.cmdpro.runology.recipe.RunicCauldronFluidRecipe;
import com.cmdpro.runology.recipe.RunicCauldronItemRecipe;
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
    public static final RecipeType RUNICCAULDRONITEMRECIPE = RecipeType.create(Runology.MOD_ID, RunicCauldronItemRecipe.Type.ID, RunicCauldronItemRecipe.class);
    public static final RecipeType RUNICCAULDRONFLUIDRECIPE = RecipeType.create(Runology.MOD_ID, RunicCauldronFluidRecipe.Type.ID, RunicCauldronFluidRecipe.class);
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new RunicCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new RunicCauldronItemRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new RunicCauldronFluidRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<IRunicRecipe> recipes = rm.getAllRecipesFor(RecipeInit.RUNICCRAFTING.get());
        registration.addRecipes(RUNICRECIPE, recipes);
        List<RunicCauldronItemRecipe> recipes2 = rm.getAllRecipesFor(RunicCauldronItemRecipe.Type.INSTANCE);
        registration.addRecipes(RUNICCAULDRONITEMRECIPE, recipes2);
        List<RunicCauldronFluidRecipe> recipes3 = rm.getAllRecipesFor(RunicCauldronFluidRecipe.Type.INSTANCE);
        registration.addRecipes(RUNICCAULDRONFLUIDRECIPE, recipes3);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ItemInit.RUNICWORKBENCHITEM.get()), RUNICRECIPE);
        registration.addRecipeCatalyst(new ItemStack(ItemInit.RUNICCAULDRONITEM.get()), RUNICCAULDRONITEMRECIPE);
        registration.addRecipeCatalyst(new ItemStack(ItemInit.RUNICCAULDRONITEM.get()), RUNICCAULDRONFLUIDRECIPE);
    }
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

        runTime = jeiRuntime;

    }
}
