package com.cmdpro.runology.recipe;


import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.RecipeInit;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class ShapelessRunicRecipe implements IRunicRecipe {
    private final ResourceLocation id;
    private final String entry;
    private final Map<String, Float> runicEnergy;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;

    public ShapelessRunicRecipe(ResourceLocation id, ItemStack result, NonNullList<Ingredient> ingredients, String entry, Map<String, Float> runicEnergy) {
        this.id = id;
        this.entry = entry;
        this.runicEnergy = runicEnergy;
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }


    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        StackedContents stackedcontents = new StackedContents();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < pInv.getContainerSize(); ++j) {
            ItemStack itemstack = pInv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    stackedcontents.accountStack(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        return i == this.ingredients.size() && (isSimple ? stackedcontents.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null);
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }


    @Override
    public ResourceLocation getId() {
        return this.id;
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

    public static class Serializer implements RecipeSerializer<ShapelessRunicRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Runology.MOD_ID,"shapelessrunicrecipe");

        @Override
        public ShapelessRunicRecipe fromJson(ResourceLocation id, JsonObject json) {
            String entry = GsonHelper.getAsString(json, "entry");
            HashMap<String, Float> runicEnergy = new HashMap<>();
            for (JsonElement i : GsonHelper.getAsJsonArray(json, "runicenergy")) {
                runicEnergy.put(GsonHelper.getAsString(i.getAsJsonObject(), "type"), GsonHelper.getAsFloat(i.getAsJsonObject(), "amount"));
            }
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > ShapedRunicRecipe.MAX_WIDTH * ShapedRunicRecipe.MAX_HEIGHT) {
                throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is " + (ShapedRunicRecipe.MAX_WIDTH * ShapedRunicRecipe.MAX_HEIGHT));
            } else {
                ItemStack itemstack = ShapedRunicRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                return new ShapelessRunicRecipe(id, itemstack, nonnulllist, entry, runicEnergy);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray pIngredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < pIngredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i), false);
                if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Override
        public ShapelessRunicRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int i = buf.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(buf));
            }

            ItemStack itemstack = buf.readItem();
            String entry = buf.readUtf();
            Map<String, Float> map = buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readFloat);
            return new ShapelessRunicRecipe(id, itemstack, nonnulllist, entry, map);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapelessRunicRecipe recipe) {
            buf.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buf);
            }

            buf.writeItem(recipe.result);
            buf.writeUtf(recipe.entry);
            buf.writeMap(recipe.runicEnergy, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeFloat);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}