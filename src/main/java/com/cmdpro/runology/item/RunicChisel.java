package com.cmdpro.runology.item;

import com.cmdpro.runology.registry.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RunicChisel extends Item {
    public RunicChisel(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        copy.setDamageValue(stack.getDamageValue() + 1);
        if (copy.getDamageValue() >= copy.getMaxDamage()) {
            return new ItemStack(ItemRegistry.GOLD_CHISEL.get());
        }
        return copy;
    }
}
