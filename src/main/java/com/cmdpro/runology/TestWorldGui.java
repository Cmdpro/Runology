package com.cmdpro.runology;

import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.databank.worldgui.WorldGuiType;
import com.cmdpro.runology.registry.WorldGuiRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;

public class TestWorldGui extends WorldGui {
    public boolean testBool = false;
    public boolean testBool2 = false;

    public TestWorldGui(WorldGuiEntity entity) {
        super(entity);
    }

    @Override
    public WorldGuiType getType() {
        return WorldGuiRegistry.TEST.get();
    }

    @Override
    public void sendData(CompoundTag compoundTag) {
        compoundTag.putBoolean("testBool", testBool);
        compoundTag.putBoolean("testBool2", testBool2);
    }

    @Override
    public void recieveData(CompoundTag compoundTag) {
        testBool = compoundTag.getBoolean("testBool");
        testBool2 = compoundTag.getBoolean("testBool2");
    }

    @Override
    public void drawGui(GuiGraphics guiGraphics) {
        Vec2 normal = getClientTargetNormal();
        int color1 = 0xFFaaaaaa;
        int color2 = 0xFFaaaaaa;
        if (normal != null) {
            int x = normalXIntoGuiX(normal.x);
            int y = normalYIntoGuiY(normal.y);
            if (isPosInBounds(x, y, 40, 20, 80, 40)) {
                color1 = 0xFFFFFFFF;
            }
            if (isPosInBounds(x, y, 20, 60, 60, 80)) {
                color2 = 0xFFFFFFFF;
            }
        }
        guiGraphics.fill(0, 0, 50, 100, testBool ? 0xFFFF0000 : 0xFF000000);
        guiGraphics.fill(50, 0, 100, 100, testBool2 ? 0xFF00FF00 : 0xFF000000);
        guiGraphics.fill(49, 20, 80, 40, color1);
        guiGraphics.fill(20, 60, 60, 80, color2);
    }

    @Override
    public void leftClick(Player player, int x, int y) {
        if (!player.level().isClientSide) {
            if (isPosInBounds(x, y, 40, 20, 80, 40)) {
                testBool = !testBool;
                sync();
            }
            if (isPosInBounds(x, y, 20, 60, 60, 80)) {
                testBool2 = !testBool2;
                sync();
            }
        }
    }

    @Override
    public void rightClick(Player player, int x, int y) {
        if (!player.level().isClientSide) {

        }
    }
}
