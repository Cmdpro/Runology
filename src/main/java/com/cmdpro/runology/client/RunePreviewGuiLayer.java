package com.cmdpro.runology.client;

import com.cmdpro.runology.data.runechiseling.RuneChiselingResult;
import com.cmdpro.runology.data.runechiseling.RuneChiselingResultManager;
import com.cmdpro.runology.item.RunicChisel;
import com.cmdpro.runology.registry.DataComponentRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

public class RunePreviewGuiLayer implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null && mc.player.isHolding((stack) -> stack.getItem() instanceof RunicChisel)) {
            if (mc.hitResult instanceof BlockHitResult hitResult) {
                Block block = mc.level.getBlockState(hitResult.getBlockPos()).getBlock();
                InteractionHand hand = InteractionHand.MAIN_HAND;
                if (!(mc.player.getItemInHand(hand).getItem() instanceof RunicChisel)) {
                    if (!(mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof RunicChisel)) {
                        hand = null;
                    } else {
                        hand = InteractionHand.OFF_HAND;
                    }
                }
                if (hand == null) {
                    return;
                }
                Optional<RuneChiselingResult> result = RuneChiselingResultManager.getResult(block, mc.player.getItemInHand(hand).get(DataComponentRegistry.RUNE));
                if (result.isPresent()) {
                    int centerX = guiGraphics.guiWidth() / 2;
                    int centerY = guiGraphics.guiHeight() / 2;
                    guiGraphics.renderItem(new ItemStack(result.get().output), (centerX + 16)-8, centerY-8);
                }
            }
        }
    }
}
