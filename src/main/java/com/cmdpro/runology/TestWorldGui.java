package com.cmdpro.runology;

import com.cmdpro.databank.ClientDatabankUtils;
import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.databank.worldgui.WorldGuiType;
import com.cmdpro.runology.registry.WorldGuiRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TestWorldGui extends WorldGui {
    public boolean active = false;
    public int time = 0;
    public List<String> text;

    public TestWorldGui(WorldGuiEntity entity) {
        super(entity);
        text = ClientHandler.getText();
    }

    @Override
    public WorldGuiType getType() {
        return WorldGuiRegistry.TEST.get();
    }

    @Override
    public List<Matrix3f> getMatrixs() {
        List<Matrix3f> matrixs = new ArrayList<>();
        addMatrixsForFacingPlayer(matrixs, true, false);
        return matrixs;
    }

    @Override
    public void sendData(CompoundTag compoundTag) {
        compoundTag.putBoolean("active", active);
        compoundTag.putInt("time", time);
    }

    @Override
    public void recieveData(CompoundTag compoundTag) {
        active = compoundTag.getBoolean("active");
        time = compoundTag.getInt("time");
    }

    @Override
    public void drawGui(GuiGraphics guiGraphics) {
        Vec2 normal = getClientTargetNormal();
        if (!active) {
            int buttonColor = 0xFFaaaaaa;
            int buttonOutlineColor = 0xFFaaaaaa;
            int buttonCenterX = 250;
            int buttonCenterY = 150;
            int buttonWidth = 60;
            int buttonHeight = 30;
            int textOffset = -4;
            int outlinePixels = 2;
            if (normal != null) {
                int x = normalXIntoGuiX(normal.x);
                int y = normalYIntoGuiY(normal.y);
                if (isPosInBounds(x, y, buttonCenterX-(buttonWidth/2), buttonCenterY-(buttonHeight/2), buttonCenterX+(buttonWidth/2), buttonCenterY+(buttonHeight/2))) {
                    buttonOutlineColor = 0xFFFFFFFF;
                }
            }
            guiGraphics.fill(0, 0, 500, 300, 0xFF000000);
            guiGraphics.fill(buttonCenterX-(buttonWidth/2), buttonCenterY-(buttonHeight/2), buttonCenterX+(buttonWidth/2), buttonCenterY+(buttonHeight/2), buttonOutlineColor);
            guiGraphics.fill(buttonCenterX-(buttonWidth/2)+outlinePixels, buttonCenterY-(buttonHeight/2)+outlinePixels, buttonCenterX+(buttonWidth/2)-outlinePixels, buttonCenterY+(buttonHeight/2)-outlinePixels, buttonColor);
            guiGraphics.drawCenteredString(ClientHandler.getFont(), Component.literal("Press me!"), buttonCenterX, buttonCenterY+textOffset, 0xFFFFFFFF);
        } else {
            guiGraphics.fill(0, 0, 500, 300, 0xFF000000);
            int yShift = 300-time;
            for (String i : text) {
                List<FormattedCharSequence> lines = ClientHandler.getFont().split(Component.literal(i), 480);
                for (FormattedCharSequence j : lines) {
                    guiGraphics.drawCenteredString(ClientHandler.getFont(), j, 250, yShift, 0xFFFFFFFF);
                    yShift += ClientHandler.getFont().lineHeight+2;
                }
                yShift += 4;
            }
        }
    }

    @Override
    public void leftClick(Player player, int x, int y) {
        int buttonCenterX = 250;
        int buttonCenterY = 150;
        int buttonWidth = 60;
        int buttonHeight = 30;
        if (!player.level().isClientSide) {
            if (isPosInBounds(x, y, buttonCenterX-(buttonWidth/2), buttonCenterY-(buttonHeight/2), buttonCenterX+(buttonWidth/2), buttonCenterY+(buttonHeight/2))) {
                active = true;
                sync();
            }
        }
    }

    @Override
    public void tick() {
        if (active) {
            time++;
        }
    }
    private static class ClientHandler {
        public static Font getFont() {
            return Minecraft.getInstance().font;
        }
        public static List<String> getText() {
            List<String> text = new ArrayList<>();
            Path path = Minecraft.getInstance().gameDirectory.toPath();
            path = path.resolve("silly_test.txt");
            try {
                if (!Files.exists(path)) {
                    Files.createFile(path);
                }
                text = Files.readAllLines(path);
            } catch (Exception ignored) {}
            return text;
        }
    }
}
