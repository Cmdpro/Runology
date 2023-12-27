package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.item.SoulAltarItem;
import com.cmdpro.runicarts.item.SoulPointItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SoulAltarItemRenderer extends GeoItemRenderer<SoulAltarItem> {
    public SoulAltarItemRenderer() {
        super(new SoulAltarItemModel());
    }
}