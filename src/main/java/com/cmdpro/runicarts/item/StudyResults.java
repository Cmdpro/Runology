package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.moddata.PlayerModData;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import net.minecraft.ChatFormatting;
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

public class StudyResults extends Item {
    public StudyResults(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (pStack.hasTag()) {
            if (pStack.getTag().contains("studytype") && pStack.getTag().contains("studyamount")) {
                int rewardType = pStack.getTag().getInt("studytype");
                int rewardAmount = pStack.getTag().getInt("studyamount");
                Component reward = Component.translatable("item.runicarts.studyresults.nothing");
                if (rewardType == 1) {
                    reward = Component.translatable("item.runicarts.studyresults.knowledge");
                }
                if (rewardType == 2) {
                    reward = Component.translatable("item.runicarts.studyresults.ancientknowledge");
                }
                if (rewardAmount > 0) {
                    pTooltipComponents.add(Component.translatable("item.runicarts.studyresults.rewardwithamount", reward, rewardAmount).withStyle(ChatFormatting.GRAY));
                } else {
                    pTooltipComponents.add(Component.translatable("item.runicarts.studyresults.reward", reward).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            ItemStack pStack = pPlayer.getItemInHand(pUsedHand);
            if (pStack.hasTag()) {
                if (pStack.getTag().contains("studytype") && pStack.getTag().contains("studyamount")) {
                    int rewardType = pStack.getTag().getInt("studytype");
                    int rewardAmount = pStack.getTag().getInt("studyamount");
                    pPlayer.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent((data) -> {
                        if (rewardType == 1) {
                            pPlayer.sendSystemMessage(Component.translatable("object.runicarts.knowledge", rewardAmount).withStyle(ChatFormatting.GREEN));
                            data.setKnowledge(data.getKnowledge() + rewardAmount);
                        }
                        if (rewardType == 2) {
                            pPlayer.sendSystemMessage(Component.translatable("object.runicarts.ancientknowledge", rewardAmount).withStyle(ChatFormatting.GREEN));
                            data.setAncientKnowledge(data.getAncientKnowledge() + rewardAmount);
                        }
                    });
                    pStack.shrink(1);
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
