package com.cmdpro.runicarts.criteriatriggers;


import com.cmdpro.runicarts.RunicArts;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class KillSoulKeeperTrigger extends SimpleCriterionTrigger<KillSoulKeeperTrigger.TriggerInstance> {

    public static final ResourceLocation ID = new ResourceLocation(RunicArts.MOD_ID, "killsoulkeeper");

    public ResourceLocation getId() {
        return ID;
    }

    public KillSoulKeeperTrigger.TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pEntityPredicate, DeserializationContext pConditionsParser) {
        return new KillSoulKeeperTrigger.TriggerInstance(pEntityPredicate);
    }

    public void trigger(ServerPlayer pPlayer) {
        trigger(pPlayer, instance -> instance.test());
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public TriggerInstance(ContextAwarePredicate pPlayer) {
            super(KillSoulKeeperTrigger.ID, pPlayer);
        }
        public boolean test() {
            return true;
        }
    }
}