package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.ISoulcastersCrystal;
import com.cmdpro.runicarts.api.RunicArtsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IceCrystal extends Item implements ISoulcastersCrystal {

    public IceCrystal(Properties properties) {
        super(properties);
        RunicArtsUtil.SOULCASTER_CRYSTALS.add(this);
    }
    @Override
    public String getId() {
        return RunicArts.MOD_ID + ":ice";
    }
}
