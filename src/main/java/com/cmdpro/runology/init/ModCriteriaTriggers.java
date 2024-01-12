package com.cmdpro.runology.init;

import com.cmdpro.runology.criteriatriggers.InstabilityEventTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class ModCriteriaTriggers {
    public static final InstabilityEventTrigger INSTABILITY_EVENT = new InstabilityEventTrigger();
    public static void register() {
        CriteriaTriggers.register(INSTABILITY_EVENT);
    }
}
