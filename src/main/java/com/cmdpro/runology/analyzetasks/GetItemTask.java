package com.cmdpro.runology.analyzetasks;

import com.cmdpro.runology.api.AnalyzeTask;
import com.cmdpro.runology.api.AnalyzeTaskSerializer;
import com.cmdpro.runology.init.AnalyzeTaskInit;
import com.cmdpro.runology.init.ItemInit;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GetItemTask extends AnalyzeTask {
    @Override
    public void render(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }

    @Override
    public void renderPost(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }

    @Override
    public boolean canComplete(Player player) {
        return player.getInventory().contains(new ItemStack(ItemInit.BLANKRUNE.get()));
    }

    @Override
    public AnalyzeTaskSerializer getSerializer() {
        return AnalyzeTaskInit.GETITEM.get();
    }
}
