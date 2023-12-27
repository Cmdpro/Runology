package com.cmdpro.runicarts.item;

import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.api.IAmplifierSoulcastersCrystal;
import com.cmdpro.runicarts.api.ISoulcastersCrystal;
import net.minecraft.world.item.Item;

public class SoulCrystal extends Item implements IAmplifierSoulcastersCrystal {

    public SoulCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public int getTimesAdd() {
        return 1;
    }
}
