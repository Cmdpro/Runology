package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.item.SoulPointItem;
import com.cmdpro.runicarts.item.SpiritTankItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SpiritTankItemRenderer extends GeoItemRenderer<SpiritTankItem> {
    public SpiritTankItemRenderer() {
        super(new SpiritTankItemModel());
    }
}