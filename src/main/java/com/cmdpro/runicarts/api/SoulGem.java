package com.cmdpro.runicarts.api;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulGem extends Item {
    public int rewardType;
    public int rewardAmount;
    public int itemCost;
    public SoulGem(Properties pProperties, int rewardType, int rewardAmount, int itemCost) {
        super(pProperties.stacksTo(1));
        this.rewardType = rewardType;
        this.rewardAmount = rewardAmount;
        this.itemCost = itemCost;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        int progress = 0;
        if (pStack.hasTag()) {
            if (pStack.getTag().contains("items")) {
                ListTag tag = (ListTag)pStack.getTag().get("items");
                progress = tag.size();
            }
        }
        Component reward = Component.translatable("item.runicarts.soulgem.nothing");
        if (rewardType == 1) {
            reward = Component.translatable("item.runicarts.soulgem.knowledge");
        }
        if (rewardType == 2) {
            reward = Component.translatable("item.runicarts.soulgem.ancientknowledge");
        }
        pTooltipComponents.add(Component.translatable("item.runicarts.soulgem.itemcost", progress, itemCost).withStyle(ChatFormatting.GRAY));
        if (rewardAmount > 0) {
            pTooltipComponents.add(Component.translatable("item.runicarts.soulgem.rewardwithamount", reward, rewardAmount).withStyle(ChatFormatting.GRAY));
        } else {
            pTooltipComponents.add(Component.translatable("item.runicarts.soulgem.reward", reward).withStyle(ChatFormatting.GRAY));
        }
    }

}
