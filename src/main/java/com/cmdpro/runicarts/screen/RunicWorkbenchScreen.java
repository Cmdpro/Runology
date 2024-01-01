package com.cmdpro.runicarts.screen;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.RunicArtsUtil;
import com.cmdpro.runicarts.api.RunicEnergyType;
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
import java.util.Set;

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
        int x2 = x+7;
        int y2 = y+36;
        Map.Entry<String, Float>[] entrySet = menu.blockEntity.getRunicEnergy().entrySet().toArray(new Map.Entry[0]);
        for (int i = 0; i < entrySet.length; i++) {
            Map.Entry<String, Float> entry = entrySet[i];
            RunicEnergyType type = RunicArtsUtil.RUNIC_ENERGY_TYPES_REGISTRY.get().getValue(ResourceLocation.tryParse(entry.getKey()));
            RenderSystem.setShaderColor((float)type.color.getRed()/255f, (float)type.color.getGreen()/255f, (float)type.color.getBlue()/255f, 1.0F);
            pGuiGraphics.blit(TEXTURE, x2, y2, 176, 0, 4, 4);
            x2 += 7;
            if (x2 > x+(7*3)) {
                x2 -= 7*3;
                y2 += 7;
            }
        }
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
        List<FormattedCharSequence> component = new ArrayList<>();
        int x2 = x+7;
        int y2 = y+36;
        Map.Entry<String, Float>[] entrySet = menu.blockEntity.getRunicEnergy().entrySet().toArray(new Map.Entry[0]);
        for (int i = 0; i < entrySet.length; i++) {
            Map.Entry<String, Float> entry = entrySet[i];
            if (pMouseX >= x2 && pMouseY >= y2 && pMouseX <= x2 + 4 && pMouseY <= y2+4) {
                component.add(Component.translatable("container.runicarts.runicworkbench.runicenergyamount", entry.getValue(), 1000, Component.translatable(Util.makeDescriptionId("rune", ResourceLocation.tryParse(entry.getKey())))).getVisualOrderText());
            }
            x2 += 7;
            if (x2 > x+(7*3)) {
                x2 -= 7*3;
                y2 += 7;
            }
        }
        if (component != null) {
            pGuiGraphics.renderTooltip(font, component, pMouseX, pMouseY);
        }
    }
}
