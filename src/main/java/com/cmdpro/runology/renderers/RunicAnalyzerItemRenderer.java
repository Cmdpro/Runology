package com.cmdpro.runology.renderers;

import com.cmdpro.runology.item.RunicAnalyzerItem;
import com.cmdpro.runology.item.RunicWorkbenchItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RunicAnalyzerItemRenderer extends GeoItemRenderer<RunicAnalyzerItem> {
    public RunicAnalyzerItemRenderer() {
        super(new RunicAnalyzerItemModel());
    }
}