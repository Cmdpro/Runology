package com.cmdpro.runology.worldgui.components;

import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.components.WorldGuiComponent;
import com.cmdpro.databank.worldgui.components.types.WorldGuiButtonComponent;
import com.cmdpro.databank.worldgui.components.types.WorldGuiButtonComponentType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

public class TestButtonComponentType extends WorldGuiButtonComponentType {

    @Override
    public WorldGuiComponent createComponent(WorldGui worldGui) {
        return new TestButtonComponent(worldGui, 0, 0, 0, 0);
    }
}
