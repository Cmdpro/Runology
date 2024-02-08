package com.cmdpro.runology.screen.slot;

import com.cmdpro.runology.api.Gauntlet;
import com.cmdpro.runology.api.Staff;
import com.cmdpro.runology.api.UpgradeItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CastingUpgradeSlot extends Slot {
    public CastingUpgradeSlot(Container itemHandler, int index, int x, int y) {
        super(itemHandler, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof UpgradeItem;
    }
}