package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.item.DivinationTableItem;
import com.cmdpro.runicarts.item.SoulAltarItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DivinationTableItemRenderer extends GeoItemRenderer<DivinationTableItem> {
    public DivinationTableItemRenderer() {
        super(new DivinationTableItemModel());
    }
}