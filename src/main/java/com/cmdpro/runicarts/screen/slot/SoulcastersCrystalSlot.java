package com.cmdpro.runicarts.screen.slot;

import com.cmdpro.runicarts.api.ISoulcastersCrystal;
import com.cmdpro.runicarts.init.ItemInit;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SoulcastersCrystalSlot extends Slot {
    public SoulcastersCrystalSlot(Container itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ISoulcastersCrystal;
    }
}