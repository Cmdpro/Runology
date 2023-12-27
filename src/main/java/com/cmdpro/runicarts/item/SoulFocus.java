package com.cmdpro.runicarts.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SoulFocus extends Item {

    public SoulFocus(Properties properties) {
        super(properties);
    }
    @Override
    public void appendHoverText(ItemStack item, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(item, level, components, flag);
        String recipe = "none";
        if (item.hasTag()) {
            if (item.getOrCreateTag().contains("recipe")) {
                recipe = item.getOrCreateTag().getString("recipe").replace(':', '.');
            }
        }
        components.add(Component.translatable("item.runicarts.soulfocus." + recipe).withStyle(ChatFormatting.GRAY));
    }
}
