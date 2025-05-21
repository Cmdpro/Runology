package com.cmdpro.runology;

import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.databank.worldgui.WorldGuiType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec2;

public class TestWorldGuiType extends WorldGuiType {
    @Override
    public WorldGui createGui(WorldGuiEntity worldGuiEntity) {
        return new TestWorldGui(worldGuiEntity);
    }

    @Override
    public Vec2 getMenuWorldSize(WorldGuiEntity worldGuiEntity) {
        return new Vec2(1, 1);
    }

    @Override
    public Vec2 getRenderSize() {
        return new Vec2(100, 100);
    }
}
