package com.cmdpro.runology.data.shatterupgrades;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Optional;

public class ShatterUpgrade {
    public ResourceLocation id;
    public ResourceLocation multiblock;
    public int power;
    public int stability;
    public ShatterUpgrade(ResourceLocation id, ResourceLocation multiblock, int power, int stability) {
        this.id = id;
        this.multiblock = multiblock;
        this.power = power;
        this.stability = stability;
    }
}
