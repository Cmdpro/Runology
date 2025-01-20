package com.cmdpro.runology.data.runetypes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.Optional;

public class RuneType {
    public ResourceLocation id;
    public Optional<ResourceLocation> requiredAdvancement;
    public Color color;
    public Component name;
    public RuneType(ResourceLocation id, Optional<ResourceLocation> requiredAdvancement, Color color, Component name) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.requiredAdvancement = requiredAdvancement;
    }
}
