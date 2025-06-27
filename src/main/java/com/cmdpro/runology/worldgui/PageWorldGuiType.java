package com.cmdpro.runology.worldgui;

import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.databank.worldgui.WorldGuiType;
import net.minecraft.world.phys.Vec2;

public class PageWorldGuiType extends WorldGuiType {
    @Override
    public WorldGui createGui(WorldGuiEntity worldGuiEntity) {
        return new PageWorldGui(worldGuiEntity);
    }

    @Override
    public Vec2 getMenuWorldSize(WorldGuiEntity worldGuiEntity) {
        return new Vec2(4, 4);
    }

    @Override
    public Vec2 getRenderSize() {
        return new Vec2(450, 450);
    }

    @Override
    public float getViewScale() {
        return 4;
    }
}
