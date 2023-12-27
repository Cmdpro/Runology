package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.item.DivinationTableItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RunicWorkbenchItemRenderer extends GeoItemRenderer<DivinationTableItem> {
    public RunicWorkbenchItemRenderer() {
        super(new RunicWorkbenchItemModel());
    }
}