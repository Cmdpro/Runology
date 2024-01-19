package com.cmdpro.runology.screen;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.AnalyzeTask;
import com.cmdpro.runology.init.ItemInit;
import com.cmdpro.runology.integration.bookconditions.BookAnalyzeTaskCondition;
import com.klikli_dev.modonomicon.book.BookEntry;
import com.klikli_dev.modonomicon.data.BookDataManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.jna.platform.win32.WinUser;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.event.InputEvent;
import org.lwjgl.system.windows.MOUSEINPUT;

import java.awt.*;

public class RunicAnalyzerScreen extends AbstractContainerScreen<RunicAnalyzerMenu> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Runology.MOD_ID, "textures/gui/runicanalyzer.png");
    public RunicAnalyzerScreen(RunicAnalyzerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        if (menu.blockEntity.item != null && menu.blockEntity.item.is(ItemInit.RESEARCH.get()) && menu.blockEntity.item.hasTag() && menu.blockEntity.item.getTag().contains("entry") && menu.blockEntity.item.getTag().contains("progress")) {
            BookEntry entry = BookDataManager.get().getBook(new ResourceLocation(Runology.MOD_ID, "runologyguide")).getEntry(ResourceLocation.tryParse(menu.blockEntity.item.getTag().getString("entry")));
            if (entry.getCondition() instanceof BookAnalyzeTaskCondition taskCondition) {
                if (menu.blockEntity.item.getTag().getInt("progress") < taskCondition.tasks.length) {
                    AnalyzeTask task = taskCondition.tasks[menu.blockEntity.item.getTag().getInt("progress")];
                    task.render(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
                    if (task.canComplete(menu.player)) {
                        if (pMouseX >= x+47 && pMouseY >= y+67 && pMouseX <= x+129 && pMouseY <= y+76) {
                            pGuiGraphics.blit(TEXTURE, x + 47, y + 67, 0, 166, 83, 10);
                        } else {
                            pGuiGraphics.blit(TEXTURE, x + 47, y + 67, 0, 176, 83, 10);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if (pMouseX >= x+47 && pMouseY >= y+67 && pMouseX <= x+129 && pMouseY <= y+76) {
            if (pButton == 0) {
                if (menu.blockEntity.item != null && menu.blockEntity.item.is(ItemInit.RESEARCH.get()) && menu.blockEntity.item.hasTag() && menu.blockEntity.item.getTag().contains("entry") && menu.blockEntity.item.getTag().contains("progress")) {
                    BookEntry entry = BookDataManager.get().getBook(new ResourceLocation(Runology.MOD_ID, "runologyguide")).getEntry(ResourceLocation.tryParse(menu.blockEntity.item.getTag().getString("entry")));
                    if (entry.getCondition() instanceof BookAnalyzeTaskCondition task) {
                        if (menu.blockEntity.item.getTag().getInt("progress") < task.tasks.length) {
                            if (task.tasks[menu.blockEntity.item.getTag().getInt("progress")].canComplete(menu.player)) {
                                menu.blockEntity.item.getTag().putInt("progress", menu.blockEntity.item.getTag().getInt("progress")+1);
                                if (menu.blockEntity.item.getTag().getInt("progress") >= task.tasks.length) {
                                    menu.blockEntity.item.getTag().putBoolean("finished", true);
                                    
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (menu.blockEntity.item != null && menu.blockEntity.item.is(ItemInit.RESEARCH.get()) && menu.blockEntity.item.hasTag() && menu.blockEntity.item.getTag().contains("entry") && menu.blockEntity.item.getTag().contains("progress")) {
            BookEntry entry = BookDataManager.get().getBook(new ResourceLocation(Runology.MOD_ID, "runologyguide")).getEntry(ResourceLocation.tryParse(menu.blockEntity.item.getTag().getString("entry")));
            if (entry.getCondition() instanceof BookAnalyzeTaskCondition task) {
                if (menu.blockEntity.item.getTag().getInt("progress") < task.tasks.length) {
                    task.tasks[menu.blockEntity.item.getTag().getInt("progress")].renderPost(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
                }
            }
        }
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
