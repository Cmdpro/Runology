package com.cmdpro.runology.worldgui.components;

import com.cmdpro.databank.multiblock.MultiblockManager;
import com.cmdpro.databank.multiblock.MultiblockRenderer;
import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.components.WorldGuiComponentType;
import com.cmdpro.databank.worldgui.components.types.WorldGuiButtonComponent;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.registry.WorldGuiComponentRegistry;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public class PageChangeComponent extends WorldGuiButtonComponent {
    public boolean isLeft;
    public static final ResourceLocation GUIDEBOOK_COMPONENTS = Runology.locate("textures/gui/guidebook_components.png");
    public PageChangeComponent(WorldGui gui, int x, int y, boolean isLeft) {
        super(gui, x-16, y-32, 32, 64);
        this.isLeft = isLeft;
    }

    public boolean shouldRender() {
        if (gui instanceof PageWorldGui gui) {
            if (isLeft) {
                return gui.page - 1 >= 0;
            } else {
                return gui.page + 1 < gui.pages.size();
            }
        }
        return false;
    }
    @Override
    public void renderNormal(GuiGraphics guiGraphics) {
        if (shouldRender()) {
            guiGraphics.blit(GUIDEBOOK_COMPONENTS, x, y, 32 + (isLeft ? 64 : 0), 0, 32, 64);
        }
    }

    @Override
    public void renderHovered(GuiGraphics guiGraphics) {
        if (shouldRender()) {
            guiGraphics.blit(GUIDEBOOK_COMPONENTS, x, y, 64 + (isLeft ? 64 : 0), 0, 32, 64);
        }
    }

    @Override
    public void leftClickButton(boolean isClient, Player player, int i, int i1) {
    }

    @Override
    public void rightClickButton(boolean isClient, Player player, int i, int i1) {
        if (gui instanceof PageWorldGui gui) {
            if (shouldRender()) {
                if (isClient) {
                    ClientHandler.playClick();
                } else {
                    if (isLeft) {
                        if (gui.page - 1 >= 0) {
                            gui.setPage(gui.page-1);
                        }
                    } else {
                        if (gui.page + 1 < gui.pages.size()) {
                            gui.setPage(gui.page+1);
                        }
                    }
                    gui.sync();
                }
            }
        }
    }

    @Override
    public WorldGuiComponentType getType() {
        return WorldGuiComponentRegistry.PAGE_CHANGE.get();
    }

    @Override
    public void sendData(CompoundTag tag) {
        super.sendData(tag);
        tag.putBoolean("isLeft", isLeft);
    }

    @Override
    public void recieveData(CompoundTag tag) {
        super.recieveData(tag);
        isLeft = tag.getBoolean("isLeft");
    }

    private static class ClientHandler {
        public static void playClick() {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
}