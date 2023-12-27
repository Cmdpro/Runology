package com.cmdpro.runicarts.screen;

import com.cmdpro.runicarts.RunicArts;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DivinationTableScreen extends AbstractContainerScreen<DivinationTableMenu> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(RunicArts.MOD_ID, "textures/gui/divinationtable.png");
    public DivinationTableScreen(DivinationTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        if (menu.getScaledProgress() > 0) {
            pGuiGraphics.blit(TEXTURE, x + 8, y + 23 + (42 - menu.getScaledProgress()), 176, (42 - menu.getScaledProgress()), 12, menu.getScaledProgress());
        }
        if (menu.blockEntity.getSouls() < 5) {
            pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("block.runicarts.divinationtable.notenoughsouls"), x + (imageWidth / 2), y + 38, 0xFF0000);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
