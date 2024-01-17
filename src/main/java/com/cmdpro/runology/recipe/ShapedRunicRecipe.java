package com.cmdpro.runology.recipe;


import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.RecipeInit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class ShapedRunicRecipe implements IRunicRecipe {
    private final ShapedRecipe recipe;
    private final String entry;
    private final Map<String, Float> runicEnergy;
    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;
    public static void setCraftingSize(int width, int height) {
        if (MAX_WIDTH < width) MAX_WIDTH = width;
        if (MAX_HEIGHT < height) MAX_HEIGHT = height;
    }

    final int width;
    final int height;
    final NonNullList<Ingredient> recipeItems;
    final ItemStack result;
    private final ResourceLocation id;
    final boolean showNotification;

    public ShapedRunicRecipe(ShapedRecipe recipe, String entry, Map<String, Float> runicEnergy) {
        this.recipe = recipe;
        this.entry = entry;
        this.runicEnergy = runicEnergy;
        this.width = recipe.getWidth();
        this.height = recipe.getHeight();
        this.recipeItems = recipe.getIngredients();
        this.result = recipe.getResultItem(RegistryAccess.EMPTY);
        this.id = recipe.getId();
        this.showNotification = recipe.showNotification();
    }

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        for(int i = 0; i <= pInv.getWidth() - this.width; ++i) {
            for(int j = 0; j <= pInv.getHeight() - this.height; ++j) {
                if (this.matches(pInv, i, j, true)) {
                    return true;
                }

                if (this.matches(pInv, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }
    private boolean matches(CraftingContainer pCraftingInventory, int pWidth, int pHeight, boolean pMirrored) {
        for(int i = 0; i < pCraftingInventory.getWidth(); ++i) {
            for(int j = 0; j < pCraftingInventory.getHeight(); ++j) {
                int k = i - pWidth;
                int l = j - pHeight;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (pMirrored) {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(pCraftingInventory.getItem(i + j * pCraftingInventory.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return this.getResultItem(pRegistryAccess).copy();
    }

    public boolean playerHasNeededEntry(Player player) {
        ConcurrentMap<ResourceLocation, Set<ResourceLocation>> entries = BookUnlockStateManager.get().saveData.getUnlockStates(player.getUUID()).readEntries;
        for (Map.Entry<ResourceLocation, Set<ResourceLocation>> i : entries.entrySet()) {
            for (ResourceLocation o : i.getValue()) {
                if (o.toString().equals(entry)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= this.width && pHeight >= this.height;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.RUNICCRAFTING.get();
    }

    @Override
    public String getEntry() {
        return entry;
    }

    @Override
    public Map<String, Float> getRunicEnergyCost() {
        return runicEnergy;
    }
    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }


    public static class Serializer implements RecipeSerializer<ShapedRunicRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Runology.MOD_ID,"shapedrunicrecipe");

        @Override
        public ShapedRunicRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(id, json);
            String entry = json.get("entry").getAsString();
            HashMap<String, Float> runicEnergy = new HashMap<>();
            for (JsonElement i : json.getAsJsonArray("runicenergy")) {
                runicEnergy.put(i.getAsJsonObject().get("type").getAsString(), i.getAsJsonObject().get("amount").getAsFloat());
            }
            return new ShapedRunicRecipe(recipe, entry, runicEnergy);
        }

        @Override
        public ShapedRunicRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buf);
            String entry = buf.readUtf();
            Map<String, Float> map = buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readFloat);
            return new ShapedRunicRecipe(recipe, entry, map);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapedRunicRecipe recipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, recipe.recipe);
            buf.writeUtf(recipe.entry);
            buf.writeMap(recipe.runicEnergy, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeFloat);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}