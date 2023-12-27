package com.cmdpro.runicarts.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class RuneItem extends Item {
    public ResourceLocation runicEnergyType;
    public RuneItem(Properties pProperties, ResourceLocation runicEnergyType) {
        super(pProperties);
        this.runicEnergyType = runicEnergyType;
    }
}
