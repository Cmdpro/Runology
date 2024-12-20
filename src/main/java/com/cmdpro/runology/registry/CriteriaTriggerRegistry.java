package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.criteriatriggers.FindShatterTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CriteriaTriggerRegistry {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES,
            Runology.MODID);
    public static final Supplier<FindShatterTrigger> FIND_SHATTER = register("find_shatter", () -> new FindShatterTrigger());
    private static <T extends CriterionTrigger<?>> Supplier<T> register(final String name,
                                                                    final Supplier<? extends T> trigger) {
        return TRIGGERS.register(name, trigger);
    }
}
