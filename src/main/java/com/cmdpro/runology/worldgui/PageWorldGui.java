package com.cmdpro.runology.worldgui;

import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.databank.worldgui.WorldGuiType;
import com.cmdpro.databank.worldgui.components.WorldGuiComponent;
import com.cmdpro.runology.api.RunologyRegistries;
import com.cmdpro.runology.api.guidebook.Page;
import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.EntryManager;
import com.cmdpro.runology.entity.RunicCodex;
import com.cmdpro.runology.registry.WorldGuiRegistry;
import com.cmdpro.runology.worldgui.components.ExitPageComponent;
import com.cmdpro.runology.worldgui.components.PageChangeComponent;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PageWorldGui extends WorldGui {
    public RunicCodex codex;
    public int time = 0;
    public int page;
    public ResourceLocation entry;
    public List<Page> pages = new ArrayList<>();
    public List<WorldGuiComponent> pageComponents = new ArrayList<>();
    public PageWorldGui(WorldGuiEntity entity) {
        super(entity);
    }

    @Override
    public void addInitialComponents() {
        addComponent(new PageChangeComponent(this, getMiddleX()-150, getMiddleY(), true));
        addComponent(new PageChangeComponent(this, getMiddleX()+150, getMiddleY(), false));
        addComponent(new ExitPageComponent(this, getMiddleX(), getMiddleY()+125));
    }

    @Override
    public WorldGuiType getType() {
        return WorldGuiRegistry.PAGE.get();
    }

    @Override
    public List<Matrix3f> getMatrixs() {
        List<Matrix3f> matrixs = new ArrayList<>();
        addMatrixsForFacingPlayer(matrixs, true, false);
        return matrixs;
    }

    @Override
    public void sendData(CompoundTag compoundTag) {
        ListTag pages = new ListTag();
        if (!this.pages.isEmpty()) {
            for (Page i : this.pages) {
                CompoundTag page = new CompoundTag();
                var result = i.getSerializer().getCodec().codec().encodeStart(NbtOps.INSTANCE, i);
                if (result.result().orElse(null) instanceof Tag tag) {
                    page.put("pageData", tag);
                }
                page.putString("serializer", RunologyRegistries.PAGE_TYPE_REGISTRY.getKey(i.getSerializer()).toString());
                pages.add(page);
            }
            compoundTag.put("pages", pages);
        }
        compoundTag.putInt("page", page);
        if (entry != null) {
            compoundTag.putString("entry", entry.toString());
        }
    }

    @Override
    public void recieveData(CompoundTag compoundTag) {
        this.page = compoundTag.getInt("page");
        if (compoundTag.contains("pages")) {
            List<Page> finalPages = new ArrayList<>();
            ListTag pages = (ListTag) compoundTag.get("pages");
            for (Tag i : pages) {
                if (i instanceof CompoundTag tag) {
                    ResourceLocation serializer = ResourceLocation.tryParse(tag.getString("serializer"));
                    PageSerializer type = RunologyRegistries.PAGE_TYPE_REGISTRY.get(serializer);
                    var obj = type.getCodec().codec().parse(NbtOps.INSTANCE, tag.get("pageData")).result().orElse(null);
                    if (obj instanceof Page page) {
                        finalPages.add(page);
                    }
                }
            }
            this.pages.clear();
            this.pages.addAll(finalPages);
        }
        if (compoundTag.contains("entry")) {
            entry = ResourceLocation.tryParse(compoundTag.getString("entry"));
        }
    }
    public void setPage(int page) {
        if (this.pages.size() > this.page) {
            this.page = page;
            pageComponents.forEach(this::removeComponent);
            pageComponents.clear();
            Page pageObj = this.pages.get(this.page);
            List<WorldGuiComponent> components = pageObj.addComponents(this, getMiddleX(), getMiddleY());
            pageComponents.addAll(components);
            components.forEach(this::addComponent);
        }
    }
    @Override
    public void rightClick(boolean isClient, Player player, int x, int y) {
        if (pages.size() > this.page) {
            Page page = pages.get(this.page);
            page.onClick(this, isClient, player, x, y, Page.ClickType.RIGHT, getMiddleX(), getMiddleY());
        }
        super.rightClick(isClient, player, x, y);
    }
    @Override
    public void leftClick(boolean isClient, Player player, int x, int y) {
        if (pages.size() > this.page) {
            Page page = pages.get(this.page);
            page.onClick(this, isClient, player, x, y, Page.ClickType.LEFT, getMiddleX(), getMiddleY());
        }
        super.rightClick(isClient, player, x, y);
    }

    @Override
    public void renderGui(GuiGraphics guiGraphics) {
        Vec2 renderSize = getType().getRenderSize();
        int outlineSize = 5;
        int guiWidth = 175;
        int guiHeight = 200;
        int guiX = (int)(renderSize.x-guiWidth)/2;
        int guiY = (int)(renderSize.y-guiHeight)/2;
        int horizontalSpikes = 4;
        int verticalSpikes = 6;
        int pixelScale = 15;
        int spikeHeight = 70;
        drawSpikes(guiGraphics, horizontalSpikes, verticalSpikes, 0, 0xFF00FF00, guiWidth, guiHeight, guiX, guiY, pixelScale, spikeHeight);
        drawSpikes(guiGraphics, horizontalSpikes, verticalSpikes, outlineSize, 0xFF000000, guiWidth, guiHeight, guiX, guiY, pixelScale, spikeHeight);

        guiGraphics.fill(guiX-outlineSize, guiY-outlineSize, guiX+guiWidth+outlineSize, guiY+guiHeight+outlineSize, 0xFF000000);

        Vec2 clientTargetNormal = getClientTargetNormal();
        int mouseX = -1;
        int mouseY = -1;
        if (clientTargetNormal != null) {
            mouseX = normalXIntoGuiX(clientTargetNormal.x);
            mouseY = normalYIntoGuiY(clientTargetNormal.y);
        }

        if (pages.size() > this.page) {
            Page page = pages.get(this.page);
            page.render(this, guiGraphics, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false), mouseX, mouseY, getMiddleX(), getMiddleY());
        }
        renderComponents(guiGraphics);
        if (entry != null) {
            guiGraphics.drawCenteredString(ClientHandler.getFont(), EntryManager.entries.get(entry).name.copy().withStyle(ChatFormatting.BOLD), getMiddleX(), guiY-32, 0xFFFFFFFF);
        }
        if (pages.size() > this.page) {
            Page page = pages.get(this.page);
            page.renderPost(this, guiGraphics, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false), mouseX, mouseY, getMiddleX(), getMiddleY());
        }
    }
    public int getMiddleX() {
        Vec2 renderSize = getType().getRenderSize();
        return (int)(renderSize.x/2f);
    }
    public int getMiddleY() {
        Vec2 renderSize = getType().getRenderSize();
        return (int)(renderSize.y/2f);
    }
    private void drawSpikes(GuiGraphics guiGraphics, int horizontalSpikes, int verticalSpikes, int shiftInward, int color, int guiWidth, int guiHeight, int guiX, int guiY, int pixelScale, int spikeHeight) {
        int mathOffset = 0;
        int offsetBounds = 10;
        float timeScale = 1f;
        Random rand = new Random(0);
        int shiftMin = -spikeHeight/5;
        int shiftMax = spikeHeight/5;
        for (float x = 0; x <= horizontalSpikes-1; x += 0.5f) {
            int spikeHeightShift = rand.nextInt(shiftMin, shiftMax);
            Vec2 pointA = new Vec2(guiX+(((float)guiWidth/(float)horizontalSpikes)*(float)x), guiY);
            Vec2 pointB = new Vec2(guiX+(((float)guiWidth/(float)horizontalSpikes)*((float)x+1f)), guiY);
            Vec2 pointC = new Vec2(guiX+(((float)guiWidth/(float)horizontalSpikes)*(float)x+0.5f), guiY-(spikeHeight+spikeHeightShift));
            pointC = pointC.add(new Vec2(rand.nextInt(-offsetBounds, offsetBounds), 0));
            pointC = pointC.add(new Vec2((float)Math.sin(Math.toRadians((time*timeScale)+mathOffset)), (float)Math.cos(Math.toRadians((time*timeScale)+mathOffset))).scale(pixelScale));
            drawTriangle(guiGraphics, pointA, pointB, pointC, 0, color, shiftInward);
            timeScale = rand.nextFloat(0.5f, 2f) * (rand.nextBoolean() ? 1 : -1);
            mathOffset += rand.nextInt(-360, 360);
        }
        for (float x = 0; x <= horizontalSpikes-1; x += 0.5f) {
            int spikeHeightShift = rand.nextInt(shiftMin, shiftMax);
            Vec2 pointA = new Vec2(guiX+(((float)guiWidth/(float)horizontalSpikes)*(float)x), guiY+guiHeight);
            Vec2 pointB = new Vec2(guiX+(((float)guiWidth/(float)horizontalSpikes)*((float)x+1f)), guiY+guiHeight);
            Vec2 pointC = new Vec2(guiX+(((float)guiWidth/(float)horizontalSpikes)*(float)x+0.5f), guiY+guiHeight+spikeHeight+spikeHeightShift);
            pointC = pointC.add(new Vec2(rand.nextInt(-offsetBounds, offsetBounds), 0));
            pointC = pointC.add(new Vec2((float)Math.sin(Math.toRadians((time*timeScale)+mathOffset)), (float)Math.cos(Math.toRadians((time*timeScale)+mathOffset))).scale(pixelScale));
            drawTriangle(guiGraphics, pointC, pointB, pointA, 0, color, shiftInward);
            timeScale = rand.nextFloat(0.5f, 2f) * (rand.nextBoolean() ? 1 : -1);
            mathOffset += rand.nextInt(-360, 360);
        }

        for (float y = 0; y <= verticalSpikes-1; y += 0.5f) {
            int spikeHeightShift = rand.nextInt(shiftMin, shiftMax);
            Vec2 pointA = new Vec2(guiX, guiY+(((float)guiHeight/(float)verticalSpikes)*(float)y));
            Vec2 pointB = new Vec2(guiX, guiY+(((float)guiHeight/(float)verticalSpikes)*((float)y+1f)));
            Vec2 pointC = new Vec2(guiX-(spikeHeight+spikeHeightShift), guiY+(((float)guiHeight/(float)verticalSpikes)*(float)y+0.5f));
            pointC = pointC.add(new Vec2(0, rand.nextInt(-offsetBounds, offsetBounds)));
            pointC = pointC.add(new Vec2((float)Math.sin(Math.toRadians((time*timeScale)+mathOffset)), (float)Math.cos(Math.toRadians((time*timeScale)+mathOffset))).scale(pixelScale));
            drawTriangle(guiGraphics, pointC, pointB, pointA, 0, color, shiftInward);
            timeScale = rand.nextFloat(0.5f, 2f) * (rand.nextBoolean() ? 1 : -1);
            mathOffset += rand.nextInt(-360, 360);
        }
        for (float y = 0; y <= verticalSpikes-1; y += 0.5f) {
            int spikeHeightShift = rand.nextInt(shiftMin, shiftMax);
            Vec2 pointA = new Vec2(guiX+guiWidth, guiY+(((float)guiHeight/(float)verticalSpikes)*(float)y));
            Vec2 pointB = new Vec2(guiX+guiWidth, guiY+(((float)guiHeight/(float)verticalSpikes)*((float)y+1f)));
            Vec2 pointC = new Vec2(guiX+guiWidth+spikeHeight+spikeHeightShift, guiY+(((float)guiHeight/(float)verticalSpikes)*(float)y+0.5f));
            pointC = pointC.add(new Vec2(0, rand.nextInt(-offsetBounds, offsetBounds)));
            pointC = pointC.add(new Vec2((float)Math.sin(Math.toRadians((time*timeScale)+mathOffset)), (float)Math.cos(Math.toRadians((time*timeScale)+mathOffset))).scale(pixelScale));
            drawTriangle(guiGraphics, pointA, pointB, pointC, 0, color, shiftInward);
            timeScale = rand.nextFloat(0.5f, 2f) * (rand.nextBoolean() ? 1 : -1);
            mathOffset += rand.nextInt(-360, 360);
        }
        int cornerWidth = 60;
        for (int i = 0; i < 4; i++) {
            int spikeHeightShift = rand.nextInt(shiftMin, shiftMax);
            Vec2 pointA = new Vec2(guiX, guiY);
            Vec2 pointB = new Vec2(guiX, guiY);
            Vec2 pointC = new Vec2(guiX, guiY);
            if (i == 0) {
                pointA = new Vec2(pointA.x, pointA.y+(cornerWidth/2));
                pointB = new Vec2(pointB.x+(cornerWidth/2), pointB.y);
                pointC = new Vec2(pointC.x, pointC.y);
            }
            if (i == 1) {
                pointA = new Vec2(guiX+guiWidth-(cornerWidth/2), pointA.y);
                pointB = new Vec2(guiX+guiWidth, pointB.y+(cornerWidth/2));
                pointC = new Vec2(guiX+guiWidth, pointC.y);
            }
            if (i == 2) {
                pointA = new Vec2(guiX+guiWidth, guiY+guiHeight-(cornerWidth/2));
                pointB = new Vec2(guiX+guiWidth-(cornerWidth/2), guiY+guiHeight);
                pointC = new Vec2(guiX+guiWidth, guiY+guiHeight);
            }
            if (i == 3) {
                pointA = new Vec2(pointA.x+(cornerWidth/2), guiY+guiHeight);
                pointB = new Vec2(pointB.x, guiY+guiHeight-(cornerWidth/2));
                pointC = new Vec2(pointC.x, guiY+guiHeight);
            }
            Vec2 offset = new Vec2((guiX+(guiWidth/2))-pointC.x, (guiY+(guiHeight/2))-pointC.y).normalized().scale(-(spikeHeight+spikeHeightShift));
            pointC = pointC.add(offset);
            pointC = pointC.add(new Vec2((float)Math.sin(Math.toRadians((time*timeScale)+mathOffset)), (float)Math.cos(Math.toRadians(timeScale+mathOffset))).scale(pixelScale));
            drawTriangle(guiGraphics, pointA, pointB, pointC, 0, color, shiftInward);
            timeScale = rand.nextFloat(0.5f, 2f) * (rand.nextBoolean() ? 1 : -1);
            mathOffset += rand.nextInt(-360, 360);
        }
    }
    private void drawTriangle(GuiGraphics graphics, Vec2 pointA, Vec2 pointB, Vec2 pointC, int z, int color, int shiftInward) {
        Matrix4f matrix4f = graphics.pose().last().pose();

        Vec2 center = new Vec2((pointA.x+pointB.x+pointC.x)/3f, (pointA.y+pointB.y+pointC.y)/3f);

        Vec2 insidePointA = pointA.add(new Vec2(center.x-pointA.x, center.y-pointA.y).normalized().scale(shiftInward));
        Vec2 insidePointB = pointB.add(new Vec2(center.x-pointB.x, center.y-pointB.y).normalized().scale(shiftInward));
        Vec2 insidePointC = pointC.add(new Vec2(center.x-pointC.x, center.y-pointC.y).normalized().scale(shiftInward));

        VertexConsumer vertexconsumer = graphics.bufferSource().getBuffer(RenderType.gui());
        vertexconsumer.addVertex(matrix4f, insidePointA.x, insidePointA.y, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, insidePointB.x, insidePointB.y, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, insidePointC.x, insidePointC.y, z).setColor(color);
        vertexconsumer.addVertex(matrix4f, insidePointC.x, insidePointC.y, z).setColor(color);
    }
    @Override
    public void tick() {
        time++;
    }
    private static class ClientHandler {
        public static Font getFont() {
            return Minecraft.getInstance().font;
        }
    }
}
