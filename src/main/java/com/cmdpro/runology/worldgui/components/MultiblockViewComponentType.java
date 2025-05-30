package com.cmdpro.runology.worldgui.components;

import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.components.WorldGuiComponent;
import com.cmdpro.databank.worldgui.components.types.WorldGuiButtonComponentType;

public class MultiblockViewComponentType extends WorldGuiButtonComponentType {
    @Override
    public WorldGuiComponent createComponent(WorldGui worldGui) {
        return new MultiblockViewComponent(worldGui, 0, 0, null);
    }
}
