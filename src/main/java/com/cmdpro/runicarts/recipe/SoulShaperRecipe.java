package com.cmdpro.runicarts.recipe;


import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.init.BlockInit;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.bookstate.BookUnlockStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;


public class SoulShaperRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;
    public final String entry;
    public final boolean mustRead;
    public final boolean locked;

    public SoulShaperRecipe(ResourceLocation id, ItemStack output,
                           Ingredient input, String entry, boolean mustRead, boolean locked) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.entry = entry;
        this.mustRead = mustRead;
        this.locked = locked;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.input);
        return nonnulllist;
    }
    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }


    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SoulShaperRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "soulshaper";
    }
    public static class Serializer implements RecipeSerializer<SoulShaperRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(RunicArts.MOD_ID,Type.ID);

        @Override
        public SoulShaperRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            boolean locked = false;
            String entry = "";
            boolean mustRead = true;
            if (json.has("locked")) {
                locked = json.get("locked").getAsBoolean();
                entry = json.get("entry").getAsString();
                if (json.has("mustRead")) {
                    mustRead = json.get("mustRead").getAsBoolean();
                }
            }
            return new SoulShaperRecipe(id, output, input, entry, mustRead, locked);
        }

        @Override
        public SoulShaperRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            boolean locked = buf.readBoolean();
            String entry = buf.readUtf();
            boolean mustRead = buf.readBoolean();
            return new SoulShaperRecipe(id, output, input, entry, mustRead, locked);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, SoulShaperRecipe recipe) {
            recipe.input.toNetwork(buf);
            buf.writeItemStack(recipe.getResultItem(RegistryAccess.EMPTY), false);
            buf.writeBoolean(recipe.locked);
            buf.writeUtf(recipe.entry);
            buf.writeBoolean(recipe.mustRead);
        }

        @SuppressWarnings("unchecked") // Need this wrapper, because generics
        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>)cls;
        }
    }
}