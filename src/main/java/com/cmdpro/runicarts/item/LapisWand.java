package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.ISoulcastersCrystal;
import com.cmdpro.runicarts.api.RunicArtsUtil;
import com.cmdpro.runicarts.api.Wand;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LapisWand extends Wand {

    public LapisWand(Properties properties) {
        super(properties, 2);
    }

    @Override
    public float getCastCostMultiplier(Player player, ItemStack stack) {
        float mult = 1-(((float)player.experienceLevel)/30f);
        if (mult <= 0.25f) {
            mult = 0.25f;
        }
        if (mult >= 1) {
            mult = 1;
        }
        return super.getCastCostMultiplier(player, stack)*mult;
    }
}
