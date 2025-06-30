package com.cmdpro.runology.integration.jade;

import com.cmdpro.databank.DatabankUtils;
import com.cmdpro.databank.hidden.types.BlockHiddenType;
import com.cmdpro.databank.worldgui.WorldGuiEntity;
import com.cmdpro.runology.entity.RunicCodexEntry;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.*;

@WailaPlugin
public class RunologyJadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addRayTraceCallback((hitResult, accessor, accessor1) -> {
            if (accessor instanceof EntityAccessor accessor2) {
                if (accessor2.getEntity() instanceof RunicCodexEntry) {
                    return null;
                }
            }
            return accessor;
        });
    }
}
