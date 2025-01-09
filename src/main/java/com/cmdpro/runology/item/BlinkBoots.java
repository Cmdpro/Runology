package com.cmdpro.runology.item;

import com.cmdpro.runology.ClientSetupEvents;
import com.cmdpro.runology.registry.ArmorMaterialRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BlinkBoots extends ArmorItem {
    public BlinkBoots(Properties properties) {
        super(ArmorMaterialRegistry.BLINK, Type.BOOTS, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.runology.blink_boots.tooltip", ClientSetupEvents.BLINK_MAPPING.get().getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item.runology.blink_boots.tooltip_2", ClientSetupEvents.BLINK_MAPPING.get().getTranslatedKeyMessage()).withStyle(ChatFormatting.GRAY));
    }
}
