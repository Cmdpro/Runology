package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.init.ItemInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

public class ModTiers {
    public static final ForgeTier SCYTHE = new ForgeTier(4, 1800, 8f,
            4f, 15, BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ItemInit.SOULMETAL.get()));
    public static final ForgeTier PURGATORY = new ForgeTier(4, 2400, 8f,
            7f, 15, BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ItemInit.PURGATORYINGOT.get()));
}
