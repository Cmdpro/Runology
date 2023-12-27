package com.cmdpro.runicarts.client;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.moddata.ClientPlayerData;
import com.cmdpro.runicarts.init.ItemInit;
import com.cmdpro.runicarts.moddata.PlayerModData;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.wands.IWand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.fml.ModList;
import org.joml.Math;

public class ModHud {
    public static final ResourceLocation soulmeter = new ResourceLocation(RunicArts.MOD_ID,"textures/hud/soulmeter.png");
    public static final ResourceLocation soulmeterbg = new ResourceLocation(RunicArts.MOD_ID,"textures/hud/soulmeterbg.png");
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void drawHUD(ForgeGui gui, GuiGraphics guiGraphics, float pt, int width,
                               int height) {
        float souls = ClientPlayerData.getPlayerSouls();
        float maxsouls = PlayerModData.getMaxSouls(minecraft.player);
        int x = width / 2;
        int y = height;

        int xpos = 3;
        int ypos = 3;
        if (ModList.get().isLoaded("magicaladventure")) {
            ypos += 15;
        }
        if (ModList.get().isLoaded("primalmagick")) {
            if (!minecraft.options.hideGui && !minecraft.player.isSpectator() && Config.SHOW_WAND_HUD.get()) {
                if (minecraft.player.getMainHandItem().getItem() instanceof IWand || minecraft.player.getOffhandItem().getItem() instanceof IWand) {
                    xpos += 60;
                }
            }
        }

        guiGraphics.blit(soulmeterbg, xpos, ypos, 0, 0, 64, 24, 64, 24);
        float w = (26*2)*(Math.clamp(0f, 1f, souls/maxsouls));
        guiGraphics.blit(soulmeter, xpos+6, ypos, 0, 0, (int)w, 24, 52, 24);
            int textx = xpos + 32;
            int texty = ypos + 8;
            String manaText = (int) Math.floor(souls) + "/" + (int) Math.floor(maxsouls);
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, manaText, textx, texty, 0x7b5480);
    }
}
