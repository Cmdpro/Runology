package com.cmdpro.runology.criteriatriggers;


import com.cmdpro.runology.Runology;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class InstabilityEventTrigger extends SimpleCriterionTrigger<InstabilityEventTrigger.TriggerInstance> {

    public static final ResourceLocation ID = new ResourceLocation(Runology.MOD_ID, "instabilityevent");

    public ResourceLocation getId() {
        return ID;
    }

    public InstabilityEventTrigger.TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pEntityPredicate, DeserializationContext pConditionsParser) {
        String instabilityEvent = pJson.has("instabilityEvent") ? GsonHelper.getAsString(pJson, "instabilityEvent") : null;
        return new InstabilityEventTrigger.TriggerInstance(pEntityPredicate, instabilityEvent == null ? null : ResourceLocation.tryParse(instabilityEvent));
    }

    public void trigger(ServerPlayer pPlayer, ResourceLocation instabilityEvent) {
        trigger(pPlayer, instance -> instance.test(instabilityEvent));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public final ResourceLocation instabilityEvent;
        public TriggerInstance(ContextAwarePredicate pPlayer, ResourceLocation instabilityEvent) {
            super(InstabilityEventTrigger.ID, pPlayer);

            this.instabilityEvent = instabilityEvent;
        }
        public boolean test(ResourceLocation instabilityEvent) {
            return (this.instabilityEvent == null || this.instabilityEvent.equals(instabilityEvent));
        }
        @Override
        public JsonObject serializeToJson(SerializationContext conditions) {
            JsonObject obj = super.serializeToJson(conditions);

            if (instabilityEvent != null)
                obj.addProperty("instabilityEvent", instabilityEvent.toString());

            return obj;
        }
    }
}