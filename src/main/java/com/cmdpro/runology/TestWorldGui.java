package com.cmdpro.runology;

import com.cmdpro.databank.ClientDatabankUtils;
import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.databank.worldgui.WorldGuiType;
import com.cmdpro.runology.registry.WorldGuiRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

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
    public List<Vec3> applyRotations() {
        List<Vec3> rotations = new ArrayList<>();
        if (entity.level().isClientSide) {
            Vec3 angle = ClientHandler.angleToClient(this);
            rotations.add(new Vec3(0, angle.y, 0));
            rotations.add(new Vec3(angle.x, 0, angle.z));
        }
        return rotations;
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
        guiGraphics.fill(40, 20, 80, 40, color1);
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
    private static class ClientHandler {
        public static Vec3 angleToClient(TestWorldGui gui) {
            Vec3 pointA = gui.entity.position().multiply(1, 1, 1);
            Vec3 pointB = Minecraft.getInstance().player.getEyePosition().multiply(1, 1, 1);
            double dX = pointA.x - pointB.x;
            double dY = 0;//pointA.y - pointB.y;
            double dZ = pointA.z - pointB.z;

            double yAngle = Math.atan2(0 - dX, 0 - dZ);
            yAngle = yAngle * (180 / Math.PI);
            yAngle = yAngle < 0 ? 360 - (-yAngle) : yAngle;

            double angle = Math.atan2(dY, Math.sqrt(dX * dX + dZ * dZ));
            angle = angle * (180 / Math.PI);
            angle = angle < 0 ? 360 - (-angle) : angle;
            return new Vec3(Math.toRadians(180), Math.toRadians((float) yAngle + 90 +90), Math.toRadians(90 - (float) angle));
        }
    }
}
