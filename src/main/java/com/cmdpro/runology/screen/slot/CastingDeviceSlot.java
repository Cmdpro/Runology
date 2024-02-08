package com.cmdpro.runology.screen.slot;

import com.cmdpro.runology.api.Gauntlet;
import com.cmdpro.runology.api.RuneItem;
import com.cmdpro.runology.api.Staff;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CastingDeviceSlot extends Slot {
    public CastingDeviceSlot(Container itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof Staff || stack.getItem() instanceof Gauntlet;
    }
}