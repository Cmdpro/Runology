package com.cmdpro.runology.worldgui;

import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.databank.worldgui.WorldGuiType;
import com.cmdpro.runology.registry.WorldGuiRegistry;
import com.cmdpro.runology.worldgui.components.TestButtonComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix3f;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TestWorldGui extends WorldGui {
    public boolean active = false;
    public int time = 0;
    public List<List<FormattedCharSequence>> text;
    public TestButtonComponent testButtonComponent;

    public TestWorldGui(WorldGuiEntity entity) {
        super(entity);
        List<String> text = ClientHandler.getText();
        this.text = new ArrayList<>();
        for (String i : text) {
            List<FormattedCharSequence> lines = ClientHandler.getFont().split(Component.literal(i), 480);
            this.text.add(lines);
        }
    }

    @Override
    public void addInitialComponents() {
        int buttonCenterX = 250;
        int buttonCenterY = 150;
        int buttonWidth = 60;
        int buttonHeight = 30;
        testButtonComponent = new TestButtonComponent(this, buttonCenterX-(buttonWidth/2), buttonCenterY-(buttonHeight/2), buttonWidth, buttonHeight);
        addComponent(testButtonComponent);
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
    public void renderGui(GuiGraphics guiGraphics) {
        if (!active) {
            guiGraphics.fill(0, 0, 500, 300, 0xFF000000);
        } else {
            guiGraphics.fill(0, 0, 500, 300, 0xFF000000);
            int yShift = 300-time;
            for (List<FormattedCharSequence> i : text) {
                for (FormattedCharSequence j : i) {
                    if (isPosInBounds(250, yShift, 0, 0, 500, 300)
                    || isPosInBounds(250, yShift+ClientHandler.getFont().lineHeight, 0, 0, 500, 300)) {
                        guiGraphics.drawCenteredString(ClientHandler.getFont(), j, 250, yShift, 0xFFFFFFFF);
                    }
                    yShift += ClientHandler.getFont().lineHeight+2;
                }
                yShift += 4;
            }
        }
        renderComponents(guiGraphics);
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
