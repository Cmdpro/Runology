package com.cmdpro.runology.data.entries.pages;

import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.ItemPageSerializer;
import com.cmdpro.runology.worldgui.PageWorldGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemPage extends TextPage {
    public ItemStack item;
    public ItemPage(Component text, ItemStack item) {
        super(text);
        this.item = item;
    }

    public List<FormattedCharSequence> tooltipToShow = new ArrayList<>();
    public boolean showTooltip;
    @Override
    public void render(PageWorldGui gui, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int middleX, int middleY) {
        super.render(gui, pGuiGraphics, pPartialTick, pMouseX, pMouseY, middleX, middleY);
        List<FormattedText> text = Minecraft.getInstance().font.getSplitter().splitLines(this.text, 200 - 8, Style.EMPTY);
        renderItemWithTooltip(pGuiGraphics, item, middleX, (middleY-10)-(((Minecraft.getInstance().font.lineHeight+2)*text.size())/2), pMouseX, pMouseY);
    }
    @Override
    public void renderPost(PageWorldGui gui, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int middleX, int middleY) {
        super.renderPost(gui, pGuiGraphics, pPartialTick, pMouseX, pMouseY, middleX, middleY);
        if (showTooltip) {
            showTooltip = false;
            pGuiGraphics.renderTooltip(Minecraft.getInstance().font, tooltipToShow, pMouseX, pMouseY);
        }
    }

    @Override
    public int textYOffset() {
        return 10;
    }

    public void renderItemWithTooltip(GuiGraphics graphics, ItemStack item, int x, int y, int mouseX, int mouseY) {
        graphics.renderItem(item, x, y);
        graphics.renderItemDecorations(Minecraft.getInstance().font, item, x, y);
        if (mouseX >= x && mouseY >= y) {
            if (mouseX <= x + 16 && mouseY <= y + 16) {
                showTooltip = true;
                tooltipToShow.clear();
                for (Component i : Screen.getTooltipFromItem(Minecraft.getInstance(), item)) {
                    tooltipToShow.add(i.getVisualOrderText());
                }
            }
        }
    }
    @Override
    public PageSerializer getSerializer() {
        return ItemPageSerializer.INSTANCE;
    }
}
