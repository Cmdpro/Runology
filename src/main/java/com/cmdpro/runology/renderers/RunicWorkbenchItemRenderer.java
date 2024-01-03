package com.cmdpro.runology.renderers;

import com.cmdpro.runology.item.RunicWorkbenchItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RunicWorkbenchItemRenderer extends GeoItemRenderer<RunicWorkbenchItem> {
    public RunicWorkbenchItemRenderer() {
        super(new RunicWorkbenchItemModel());
    }
}