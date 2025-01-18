package com.cmdpro.runology.integration.modonomicon.page;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.recipe.ShatterInfusionRecipe;
import com.cmdpro.runology.registry.RecipeRegistry;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ShatterInfusionRecipePage extends BookRecipePage<ShatterInfusionRecipe> {
    public static final ResourceLocation ID = Runology.locate("shatter_infusion_recipe");
    public ShatterInfusionRecipePage(BookTextHolder title1, ResourceLocation recipeId1, BookTextHolder title2, ResourceLocation recipeId2, BookTextHolder text, String anchor, BookCondition condition) {
        super(RecipeRegistry.SHATTER_INFUSION_TYPE.get(), title1, recipeId1, title2, recipeId2, text, anchor, condition);
    }
    public static ShatterInfusionRecipePage fromJson(ResourceLocation entryId, JsonObject json, HolderLookup.Provider provider) {
        var common = BookRecipePage.commonFromJson(json, provider);
        var anchor = GsonHelper.getAsString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(entryId, json.getAsJsonObject("condition"), provider)
                : new BookNoneCondition();
        return new ShatterInfusionRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    public static ShatterInfusionRecipePage fromNetwork(RegistryFriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        var anchor = buffer.readUtf();
        var condition = BookCondition.fromNetwork(buffer);
        return new ShatterInfusionRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    @Override
    protected ItemStack getRecipeOutput(Level level, RecipeHolder<ShatterInfusionRecipe> recipe) {
        if (recipe == null) {
            return ItemStack.EMPTY;
        }

        return recipe.value().getResultItem(level.registryAccess());
    }

    @Override
    public ResourceLocation getType() {
        return ID;
    }
}