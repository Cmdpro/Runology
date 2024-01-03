package com.cmdpro.runology.recipe;


import com.cmdpro.runology.Runology;
import com.cmdpro.runology.init.RecipeInit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
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

public class ShapedRunicRecipe extends ShapedRecipe implements IRunicRecipe {
    private final ShapedRecipe recipe;
    private final ResourceLocation entry;
    private final Map<String, Float> runicEnergy;

    public ShapedRunicRecipe(ShapedRecipe recipe, ResourceLocation entry, Map<String, Float> runicEnergy) {
        super(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem(RegistryAccess.EMPTY));
        this.recipe = recipe;
        this.entry = entry;
        this.runicEnergy = runicEnergy;
    }

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        return super.matches(pInv, pLevel);
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        return recipe.assemble(pContainer, pRegistryAccess);
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
        return true;
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
    public ResourceLocation getEntry() {
        return entry;
    }

    @Override
    public Map<String, Float> getRunicEnergyCost() {
        return runicEnergy;
    }

    public static class Serializer implements RecipeSerializer<ShapedRunicRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Runology.MOD_ID,"shapedrunicrecipe");

        @Override
        public ShapedRunicRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(id, json);
            ResourceLocation entry = ResourceLocation.of(json.get("entry").getAsString(), ':');
            HashMap<String, Float> runicEnergy = new HashMap<>();
            for (JsonElement i : json.getAsJsonArray("runicenergy")) {
                runicEnergy.put(i.getAsJsonObject().get("type").getAsString(), i.getAsJsonObject().get("amount").getAsFloat());
            }
            return new ShapedRunicRecipe(recipe, entry, runicEnergy);
        }

        @Override
        public ShapedRunicRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ResourceLocation entry = buf.readResourceLocation();
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buf);
            Map<String, Float> map = buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readFloat);
            return new ShapedRunicRecipe(recipe, entry, map);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapedRunicRecipe recipe) {
            buf.writeResourceLocation(recipe.entry);
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, recipe.recipe);
            buf.writeMap(recipe.runicEnergy, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeFloat);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}