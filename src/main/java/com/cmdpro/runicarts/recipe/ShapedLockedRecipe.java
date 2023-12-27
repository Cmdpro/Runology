package com.cmdpro.runicarts.recipe;


import com.cmdpro.runicarts.RunicArts;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.Modonomicon;
import com.klikli_dev.modonomicon.ModonomiconForge;
import com.klikli_dev.modonomicon.api.ModonomiconAPI;
import com.klikli_dev.modonomicon.api.ModonomiconConstants;
import com.klikli_dev.modonomicon.apiimpl.ModonomiconAPIImpl;
import com.klikli_dev.modonomicon.book.Book;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import com.klikli_dev.modonomicon.data.BookDataManager;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class ShapedLockedRecipe extends ShapedRecipe {
    private final ShapedRecipe recipe;
    private final String entry;
    private final boolean mustRead;

    public ShapedLockedRecipe(ShapedRecipe recipe, String entry, boolean mustRead) {
        super(recipe.getId(), recipe.getGroup(), recipe.category(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem(RegistryAccess.EMPTY));
        this.recipe = recipe;
        this.entry = entry;
        this.mustRead = mustRead;
    }

    @Override
    public boolean matches(CraftingContainer pInv, Level pLevel) {
        return super.matches(pInv, pLevel);
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        Player player = null;
        if (EffectiveSide.get().isServer()) {
            for (ServerPlayer i : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                if (i.containerMenu == ((TransientCraftingContainer) pContainer).menu && pContainer.stillValid(i)) {
                    if (player != null) {
                        return ItemStack.EMPTY;
                    }
                    player = i;
                }
            }
        } else {
            player = Minecraft.getInstance().player;
        }
        if (playerHasNeededEntry(player)) {
            return recipe.assemble(pContainer, pRegistryAccess);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public boolean playerHasNeededEntry(Player player) {
        ConcurrentMap<ResourceLocation, Set<ResourceLocation>> entries = BookUnlockStateManager.get().saveData.getUnlockStates(player.getUUID()).unlockedEntries;
        if (mustRead) {
            entries = BookUnlockStateManager.get().saveData.getUnlockStates(player.getUUID()).readEntries;
        }
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
        return RecipeType.CRAFTING;
    }

    public static class Serializer implements RecipeSerializer<ShapedLockedRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(RunicArts.MOD_ID,"shapedlockedrecipe");

        @Override
        public ShapedLockedRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromJson(id, json);
            String entry = json.get("entry").getAsString();
            boolean mustRead = true;
            if (json.has("mustRead")) {
                mustRead = json.get("mustRead").getAsBoolean();
            }
            return new ShapedLockedRecipe(recipe, entry, mustRead);
        }

        @Override
        public ShapedLockedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String entry = buf.readUtf();
            boolean mustRead = buf.readBoolean();
            ShapedRecipe recipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buf);
            return new ShapedLockedRecipe(recipe, entry, mustRead);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapedLockedRecipe recipe) {
            buf.writeUtf(recipe.entry);
            buf.writeBoolean(recipe.mustRead);
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, recipe.recipe);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}