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

public class FindShatterTrigger extends SimpleCriterionTrigger<FindShatterTrigger.FindShatterTriggerInstance> {

    public static Criterion<FindShatterTriggerInstance> instance(ContextAwarePredicate player) {
        return CriteriaTriggerRegistry.FIND_SHATTER.get().createCriterion(new FindShatterTriggerInstance(Optional.of(player)));
    }
    public static final Codec<FindShatterTriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(FindShatterTriggerInstance::player)
    ).apply(instance, FindShatterTriggerInstance::new));
    @Override
    public Codec<FindShatterTriggerInstance> codec() {
        return CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> true);
    }
    public record FindShatterTriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
    }
}