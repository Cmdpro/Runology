package com.cmdpro.runology.criteriatriggers;


import com.cmdpro.runology.registry.CriteriaTriggerRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class ExplodeShatterTrigger extends SimpleCriterionTrigger<ExplodeShatterTrigger.ExplodeShatterTriggerInstance> {

    public static Criterion<ExplodeShatterTriggerInstance> instance(ContextAwarePredicate player) {
        return CriteriaTriggerRegistry.EXPLODE_SHATTER.get().createCriterion(new ExplodeShatterTriggerInstance(Optional.of(player)));
    }
    public static final Codec<ExplodeShatterTriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ExplodeShatterTriggerInstance::player)
    ).apply(instance, ExplodeShatterTriggerInstance::new));
    @Override
    public Codec<ExplodeShatterTriggerInstance> codec() {
        return CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> true);
    }
    public record ExplodeShatterTriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
    }
}