package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.criteriatriggers.ExplodeShatterTrigger;
import com.cmdpro.runology.criteriatriggers.FindShatterTrigger;
import com.cmdpro.runology.criteriatriggers.NearbySurgeTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CriteriaTriggerRegistry {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES,
            Runology.MODID);
    public static final Supplier<FindShatterTrigger> FIND_SHATTER = register("find_shatter", () -> new FindShatterTrigger());
    public static final Supplier<NearbySurgeTrigger> NEARBY_SURGE = register("nearby_surge", () -> new NearbySurgeTrigger());
    public static final Supplier<ExplodeShatterTrigger> EXPLODE_SHATTER = register("explode_shatter", () -> new ExplodeShatterTrigger());
    private static <T extends CriterionTrigger<?>> Supplier<T> register(final String name,
                                                                    final Supplier<? extends T> trigger) {
        return TRIGGERS.register(name, trigger);
    }
}
