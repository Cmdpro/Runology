package com.cmdpro.runology.data.entries.pages;

import com.cmdpro.runology.api.guidebook.Page;
import com.cmdpro.runology.api.guidebook.PageSerializer;
import com.cmdpro.runology.data.entries.pages.serializers.TextPageSerializer;
import com.cmdpro.runology.worldgui.PageWorldGui;
import com.ibm.icu.lang.UCharacter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.FormattedBidiReorder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.*;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class TextPage extends Page {
    public TextPage(Component text) {
        this.text = text;
    }
    public Component text;
    public int textYOffset() {
        return 0;
    }
    @Override
    public void render(PageWorldGui gui, GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY, int middleX, int middleY) {
        float scale = 1f;
        MutableComponent component = this.text.copy();
        List<FormattedText> text = Minecraft.getInstance().font.getSplitter().splitLines(component, 200 - 8, Style.EMPTY);
        int offsetY = (int)((-((Minecraft.getInstance().font.lineHeight+2)*text.size())/2)*scale);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale, scale, scale);
        for (FormattedText i : text) {
            int x = middleX;
            FormattedCharSequence formattedCharSequence = FormattedBidiReorder.reorder(i, Language.getInstance().isDefaultRightToLeft());
            pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, formattedCharSequence, (int)(x/scale), (int)((middleY + offsetY + textYOffset())/scale), 0xFFFFFFFF);
            offsetY += (int)((Minecraft.getInstance().font.lineHeight+2)*scale);
        }
        pGuiGraphics.pose().popPose();
    }

    @Override
    public PageSerializer getSerializer() {
        return TextPageSerializer.INSTANCE;
    }
}
