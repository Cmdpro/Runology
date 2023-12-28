package com.cmdpro.runicarts.screen.slot;

import com.cmdpro.runicarts.api.RuneItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RuneSlot extends SlotItemHandler {
    public RuneSlot(IItemHandler itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof RuneItem;
    }
}