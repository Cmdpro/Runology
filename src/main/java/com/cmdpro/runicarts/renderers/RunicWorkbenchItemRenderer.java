package com.cmdpro.runicarts.renderers;

import com.cmdpro.runicarts.item.RunicWorkbenchItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RunicWorkbenchItemRenderer extends GeoItemRenderer<RunicWorkbenchItem> {
    public RunicWorkbenchItemRenderer() {
        super(new RunicWorkbenchItemModel());
    }
}