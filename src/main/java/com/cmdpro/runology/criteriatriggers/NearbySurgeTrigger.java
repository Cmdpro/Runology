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

public class NearbySurgeTrigger extends SimpleCriterionTrigger<NearbySurgeTrigger.NearbySurgeTriggerInstance> {

    public static Criterion<NearbySurgeTriggerInstance> instance(ContextAwarePredicate player) {
        return CriteriaTriggerRegistry.NEARBY_SURGE.get().createCriterion(new NearbySurgeTriggerInstance(Optional.of(player)));
    }
    public static final Codec<NearbySurgeTriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(NearbySurgeTriggerInstance::player)
    ).apply(instance, NearbySurgeTriggerInstance::new));
    @Override
    public Codec<NearbySurgeTriggerInstance> codec() {
        return CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> true);
    }
    public record NearbySurgeTriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
    }
}