package com.cmdpro.runology.recipe;

import com.cmdpro.runology.registry.BlockRegistry;
import com.cmdpro.runology.registry.RecipeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class OtherworldlyEnergyRecipe implements Recipe<RecipeInput> {
    private final ItemStack output;
    private final Ingredient input;

    public OtherworldlyEnergyRecipe(ItemStack output,
                                    Ingredient input) {
        this.output = output;
        this.input = input;
    }
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(input);
        return list;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistry.SHATTER.get());
    }

    @Override
    public boolean matches(RecipeInput pContainer, Level pLevel) {
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput pContainer, HolderLookup.Provider pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }
    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistryAccess) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.OTHERWORLDLY_ENERGY_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<OtherworldlyEnergyRecipe> {
        public static final MapCodec<OtherworldlyEnergyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.CODEC.fieldOf("result").forGetter(r -> r.output),
                Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input)
        ).apply(instance, OtherworldlyEnergyRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, OtherworldlyEnergyRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, obj) -> {
                    ItemStack.STREAM_CODEC.encode(buf, obj.output);
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, obj.input);
                },
                (buf) -> {
                    ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
                    Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                    return new OtherworldlyEnergyRecipe(output, input);
                }
        );

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public MapCodec<OtherworldlyEnergyRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, OtherworldlyEnergyRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}