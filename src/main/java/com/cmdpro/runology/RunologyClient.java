package com.cmdpro.runology;

import com.cmdpro.databank.DatabankClient;
import com.cmdpro.databank.impact.ImpactFrameHandler;
import com.cmdpro.databank.misc.ResizeHelper;
import com.cmdpro.databank.model.DatabankModels;
import com.cmdpro.databank.music.MusicSystem;
import com.cmdpro.databank.shaders.PostShaderInstance;
import com.cmdpro.databank.shaders.PostShaderManager;
import net.minecraft.client.Minecraft;

public class RunologyClient {
    protected static void addResizeListeners() {
        // Post-Process Shaders
        ResizeHelper.addListener((width, height) -> {
            RenderEvents.getShatterTarget().resize(width, height, Minecraft.ON_OSX);
            RenderEvents.getPlayerPowerTarget().resize(width, height, Minecraft.ON_OSX);
            RenderEvents.getSpecialBypassTarget().resize(width, height, Minecraft.ON_OSX);
        });
    }
    protected static void register() {
        addResizeListeners();
    }
}
