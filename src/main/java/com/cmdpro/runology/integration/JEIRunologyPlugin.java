package com.cmdpro.runology.integration;

import com.cmdpro.runology.Runology;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.*;

@JeiPlugin
public class JEIRunologyPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Runology.MOD_ID, "jei_plugin");
    }

    public static IJeiRuntime runTime;
    //public static final RecipeType soulAltarCategory = RecipeType.create(Runology.MOD_ID, SoulAltarRecipe.Type.ID, SoulAltarRecipe.class);
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        //registration.addRecipeCategories(new SoulAltarRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        //List<SoulAltarRecipe> recipes = rm.getAllRecipesFor(SoulAltarRecipe.Type.INSTANCE);
        //registration.addRecipes(soulAltarCategory, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        //registration.addRecipeCatalyst(new ItemStack(BlockInit.SOULALTAR.get()), soulAltarCategory);
    }
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

        runTime = jeiRuntime;

    }
}
