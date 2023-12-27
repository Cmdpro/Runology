package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AncientPage extends Item {
    public AncientPage(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);
            stack.shrink(1);
            pPlayer.sendSystemMessage(Component.translatable("object.runicarts.ancientknowledge", 1).withStyle(ChatFormatting.GREEN));
            pPlayer.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent((data) -> {
                data.setAncientKnowledge(data.getAncientKnowledge()+1);
            });
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
