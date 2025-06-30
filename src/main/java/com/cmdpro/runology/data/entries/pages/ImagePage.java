package com.cmdpro.runology.data.entries.pages;

import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.ImagePageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.ItemPageSerializer;
import com.cmdpro.runology.worldgui.PageWorldGui;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class ImagePage extends TextPage {
    public ResourceLocation image;
    public float scale;
    public int verticalOffset;
    public ImagePage(Component text, ResourceLocation image, float scale, int verticalOffset) {
        super(text);
        this.image = image;
        this.scale = scale;
        this.verticalOffset = verticalOffset;
    }
    @Override
    public void render(PageWorldGui gui, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int middleX, int middleY) {
        super.render(gui, pGuiGraphics, pPartialTick, pMouseX, pMouseY, middleX, middleY);

        Vec2 imageTop = new Vec2(middleX, middleY-100);
        float height = 100;
        float width = 100;
        height *= scale;
        width *= scale;

        Vec2 from = new Vec2(imageTop.x-(width/2f), imageTop.y+verticalOffset);
        Vec2 to = new Vec2(imageTop.x+(width/2f), imageTop.y+height+verticalOffset);

        RenderSystem.setShaderTexture(0, image);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = pGuiGraphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.addVertex(matrix4f, from.x, from.y, 0)
                .setUv(0, 0)
                .setColor(0xFFFFFFFF);
        bufferbuilder.addVertex(matrix4f, from.x, to.y, 0)
                .setUv(0, 1)
                .setColor(0xFFFFFFFF);
        bufferbuilder.addVertex(matrix4f, to.x, to.y, 0)
                .setUv(1, 1)
                .setColor(0xFFFFFFFF);
        bufferbuilder.addVertex(matrix4f, to.x, from.y, 0)
                .setUv(1, 0)
                .setColor(0xFFFFFFFF);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    @Override
    public int textYMin(int middleX, int middleY) {
        return ((middleY-100)+(int)(100*scale)+4)+verticalOffset;
    }

    @Override
    public PageSerializer getSerializer() {
        return ImagePageSerializer.INSTANCE;
    }
}
