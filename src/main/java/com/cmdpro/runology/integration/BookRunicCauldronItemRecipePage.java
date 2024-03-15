package com.cmdpro.runology.integration;

import com.cmdpro.runology.init.RecipeInit;
import com.cmdpro.runology.recipe.RunicCauldronItemRecipe;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.BookNoneCondition;
import com.klikli_dev.modonomicon.book.page.BookRecipePage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public class BookRunicCauldronItemRecipePage extends BookRecipePage<Recipe<?>> {


    public BookRunicCauldronItemRecipePage(BookTextHolder title1, ResourceLocation recipeId1, BookTextHolder title2, ResourceLocation recipeId2, BookTextHolder text, String anchor, BookCondition condition) {
        super(RunicCauldronItemRecipe.Type.INSTANCE, title1, recipeId1, title2, recipeId2, text, anchor, condition);
    }

    public static BookRunicCauldronItemRecipePage fromJson(JsonObject json) {
        var common = BookRecipePage.commonFromJson(json);
        var anchor = GsonHelper.getAsString(json, "anchor", "");
        var condition = json.has("condition")
                ? BookCondition.fromJson(json.getAsJsonObject("condition"))
                : new BookNoneCondition();
        return new BookRunicCauldronItemRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    public static BookRunicCauldronItemRecipePage fromNetwork(FriendlyByteBuf buffer) {
        var common = BookRecipePage.commonFromNetwork(buffer);
        var anchor = buffer.readUtf();
        var condition = BookCondition.fromNetwork(buffer);
        return new BookRunicCauldronItemRecipePage(common.title1(), common.recipeId1(), common.title2(), common.recipeId2(), common.text(), anchor, condition);
    }

    @Override
    protected ItemStack getRecipeOutput(Level level, Recipe<?> recipe) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getType() {
        return RunologyModonomiconConstants.Page.RUNICCAULDRONITEM;
    }
}