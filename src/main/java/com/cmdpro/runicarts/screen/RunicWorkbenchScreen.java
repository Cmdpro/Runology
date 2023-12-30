package com.cmdpro.runicarts.screen;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.RunicArtsUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemDisplayContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RunicWorkbenchScreen extends AbstractContainerScreen<RunicWorkbenchMenu> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(RunicArts.MOD_ID, "textures/gui/runicworkbench.png");
    public RunicWorkbenchScreen(RunicWorkbenchMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }
    public float time;
    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        /*
        int y2 = y+69;
        int totalHeight = 0;
        for (Map.Entry<String, Float> i : menu.blockEntity.getRunicEnergy().entrySet()) {
            int height = (int)Math.ceil((1f/(float)menu.blockEntity.getRunicEnergy().keySet().size())*32f);
            totalHeight += height;
            if (totalHeight > 32) {
                height -= totalHeight-32;
            }
            Color color = RunicArtsUtil.RUNIC_ENERGY_TYPES_REGISTRY.get().getValue(ResourceLocation.tryParse(i.getKey())).color;
            RenderSystem.setShaderColor((float)color.getRed()/255f, (float)color.getGreen()/255f, (float)color.getBlue()/255f,1f);
            pGuiGraphics.fill(x + 9, y2 - height, x + 23, y2, 0xFFFFFFFF);
            y2 -= height;
        }*/
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.25f);
        pGuiGraphics.renderItem(menu.blockEntity.item, x+124, y+35);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
        time += pPartialTick;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if (pMouseX >= x+124 && pMouseY >= y+12 && pMouseX <= x+139 && pMouseY <= y+27) {
            if (menu.blockEntity.runicEnergyCost.size() > 0) {
                List<FormattedCharSequence> tooltip = new ArrayList<>();
                for (Map.Entry<String, Float> i : menu.blockEntity.runicEnergyCost.entrySet()) {
                    tooltip.add(Component.translatable("container.runicarts.runicworkbench.runicenergycost", i.getValue(), Component.translatable(Util.makeDescriptionId("rune", ResourceLocation.tryParse(i.getKey())))).getVisualOrderText());
                }
                pGuiGraphics.renderTooltip(font, tooltip, pMouseX, pMouseY);
            }
        }
        if (pMouseX >= x+8 && pMouseY >= y+17 && pMouseX <= x+23 && pMouseY <= y+32) {
            pGuiGraphics.renderTooltip(font, Component.translatable("container.runicarts.runicworkbench.runicenergyfull", menu.blockEntity.getTotalRunicEnergy(), 1000), pMouseX, pMouseY);
        }/*
        int y2 = y+69;
        int totalHeight = 0;
        List<FormattedCharSequence> component = new ArrayList<>();
        for (Map.Entry<String, Float> i : menu.blockEntity.getRunicEnergy().entrySet()) {
            int height = (int)Math.ceil((1f/(float)menu.blockEntity.getRunicEnergy().keySet().size())*32f);
            totalHeight += height;
            if (totalHeight > 32) {
                height -= totalHeight-32;
            }
            if (pMouseX >= x + 9 && pMouseY >= y2 - height && pMouseX <= x + 23 && pMouseY <= y2) {
                component.add(Component.translatable("container.runicarts.runicworkbench.runicenergycost", i.getValue(), Component.translatable(Util.makeDescriptionId("rune", ResourceLocation.tryParse(i.getKey())))).getVisualOrderText());
                component.add(Component.translatable("container.runicarts.runicworkbench.runicenergyfull", menu.blockEntity.getTotalRunicEnergy(), 1000f).getVisualOrderText());
            }
            y2 -= height;
        }
        if (component != null) {
            pGuiGraphics.renderTooltip(font, component, pMouseX, pMouseY);
        }*/
    }
}
