package com.cmdpro.runology.worldgui.components;

import com.cmdpro.databank.multiblock.MultiblockManager;
import com.cmdpro.databank.multiblock.MultiblockRenderer;
import com.cmdpro.databank.worldgui.WorldGui;
import com.cmdpro.databank.worldgui.components.WorldGuiComponentType;
import com.cmdpro.databank.worldgui.components.types.WorldGuiButtonComponent;
import com.cmdpro.runology.registry.WorldGuiComponentRegistry;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public class MultiblockViewComponent extends WorldGuiButtonComponent {
    public ResourceLocation multiblock;
    public MultiblockViewComponent(WorldGui gui, int x, int y, ResourceLocation multiblock) {
        super(gui, x, y, 16, 16);
        this.multiblock = multiblock;
    }

    @Override
    public void renderNormal(GuiGraphics guiGraphics) {
        int buttonColor = 0xFFaaaaaa;
        int buttonOutlineColor = 0xFFaaaaaa;
        int outlinePixels = 2;
        int textOffset = -4;
        guiGraphics.fill(x, y, x+width, y+height, buttonOutlineColor);
        guiGraphics.fill(x+outlinePixels, y+outlinePixels, x+width-outlinePixels, y+height-outlinePixels, buttonColor);
        guiGraphics.drawCenteredString(ClientHandler.getFont(), Component.literal("Press me!"), x+(width/2), y+(height/2)+textOffset, 0xFFFFFFFF);
    }

    @Override
    public void renderHovered(GuiGraphics guiGraphics) {
        int buttonColor = 0xFFaaaaaa;
        int buttonOutlineColor = 0xFFFFFFFF;
        int outlinePixels = 2;
        int textOffset = -4;
        guiGraphics.fill(x, y, x+width, y+height, buttonOutlineColor);
        guiGraphics.fill(x+outlinePixels, y+outlinePixels, x+width-outlinePixels, y+height-outlinePixels, buttonColor);
        guiGraphics.drawCenteredString(ClientHandler.getFont(), Component.literal("Press me!"), x+(width/2), y+(height/2)+textOffset, 0xFFFFFFFF);
    }

    @Override
    public void leftClickButton(boolean b, Player player, int i, int i1) {
        if (gui instanceof PageWorldGui gui) {
            MultiblockRenderer.multiblockPos = null;
            MultiblockRenderer.multiblockRotation = null;
            MultiblockRenderer.multiblock = MultiblockRenderer.multiblock == null ? MultiblockManager.multiblocks.get(multiblock) : null;
            ClientHandler.playClick();
        }
    }

    @Override
    public void rightClickButton(boolean b, Player player, int i, int i1) {

    }

    @Override
    public WorldGuiComponentType getType() {
        return WorldGuiComponentRegistry.MULTIBLOCK_VIEW.get();
    }

    @Override
    public void sendData(CompoundTag tag) {
        super.sendData(tag);
        tag.putString("multiblock", multiblock.toString());
    }

    @Override
    public void recieveData(CompoundTag tag) {
        super.recieveData(tag);
        multiblock = ResourceLocation.tryParse(tag.getString("multiblock"));
    }

    private static class ClientHandler {
        public static Font getFont() {
            return Minecraft.getInstance().font;
        }
        public static void playClick() {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
}
