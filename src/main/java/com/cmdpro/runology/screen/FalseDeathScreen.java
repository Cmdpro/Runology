package com.cmdpro.runology.screen;

import com.cmdpro.runology.Runology;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class FalseDeathScreen extends DeathScreen {
    int timer = 0;
    public FalseDeathScreen(@Nullable Component causeOfDeath, boolean hardcore) {
        super(causeOfDeath, hardcore);
    }

    @Override
    public void tick() {
        super.tick();
        timer++;
        if (timer == 65) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.GLASS_BREAK, 1));
        }
        if (timer >= 70) {
            this.minecraft.popGuiLayer();
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (timer >= 20 && timer <= 50) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(-Math.sin(Math.exp(timer)) * (timer - 20), -Math.cos(Math.exp(timer)) * (timer - 20), 0);
        }
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        if (timer >= 20 && timer <= 50) {
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (timer >= 20) {
            if (timer <= 50) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(Math.sin(Math.exp(timer)) * (timer - 20), Math.cos(Math.exp(timer)) * (timer - 20), 0);
                super.render(guiGraphics, mouseX, mouseY, partialTick);
                guiGraphics.pose().popPose();
            } else if (timer <= 55) {
                super.render(guiGraphics, mouseX, mouseY, partialTick);
                guiGraphics.fill(0, 0, width, height, new Color(1f, 1f, 1f, (timer-50f)/10f).getRGB());
            } else if (timer >= 65) {
                guiGraphics.fill(0, 0, width, height, new Color(1f, 1f, 1f, 1f-((timer-65f)/10f)).getRGB());
            } else {
                guiGraphics.fill(0, 0, width, height, new Color(1f, 1f, 1f, 1f).getRGB());
            }
        } else {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean handleComponentClicked(@Nullable Style style) {
        return false;
    }
}
