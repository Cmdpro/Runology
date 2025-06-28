
package com.cmdpro.runology.worldgui.components;

import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.components.WorldGuiComponentType;
import com.cmdpro.databank.worldgui.components.types.WorldGuiButtonComponent;
import com.cmdpro.runology.Runology;
import com.cmdpro.runology.registry.WorldGuiComponentRegistry;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public class ExitPageComponent extends WorldGuiButtonComponent {
    public static final ResourceLocation GUIDEBOOK_COMPONENTS = Runology.locate("textures/gui/guidebook_components.png");
    public ExitPageComponent(WorldGui gui, int x, int y) {
        super(gui, x-(12+getHitboxExtra()), y-(10+getHitboxExtra()), 24+(getHitboxExtra()*2), 20+(getHitboxExtra()*2));
    }
    public static int getHitboxExtra() {
        return 6;
    }
    @Override
    public void renderNormal(GuiGraphics guiGraphics) {
        guiGraphics.blit(GUIDEBOOK_COMPONENTS, x+getHitboxExtra(), y+getHitboxExtra(), 160, 0, 24, 20);
    }

    @Override
    public void renderHovered(GuiGraphics guiGraphics) {
        guiGraphics.blit(GUIDEBOOK_COMPONENTS, x+getHitboxExtra(), y+getHitboxExtra(), 184, 0, 24, 20);
    }

    @Override
    public void leftClickButton(boolean isClient, Player player, int i, int i1) {
    }

    @Override
    public void rightClickButton(boolean isClient, Player player, int i, int i1) {
        if (gui instanceof PageWorldGui gui) {
            if (isClient) {
                ClientHandler.playClick();
            } else {
                gui.codex.exitEntry();
            }
        }
    }

    @Override
    public WorldGuiComponentType getType() {
        return WorldGuiComponentRegistry.EXIT_PAGE.get();
    }

    private static class ClientHandler {
        public static void playClick() {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
}