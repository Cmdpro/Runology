package com.cmdpro.runology.api;

import net.minecraft.world.item.Item;

public class EmpoweredGauntlet extends Gauntlet {
    public Item returnsTo;
    public EmpoweredGauntlet(Properties properties, int magicLevel, Item returnsTo) {
        super(properties, magicLevel);
        this.returnsTo = returnsTo;
    }
}
