package com.cmdpro.runicarts.integration.bookconditions;

import ca.weblite.objc.Client;
import com.cmdpro.runicarts.RunicArts;
import com.cmdpro.runicarts.moddata.ClientPlayerData;
import com.cmdpro.runicarts.moddata.PlayerModData;
import com.cmdpro.runicarts.moddata.PlayerModDataProvider;
import com.google.gson.JsonObject;
import com.klikli_dev.modonomicon.book.conditions.BookCondition;
import com.klikli_dev.modonomicon.book.conditions.context.BookConditionContext;
import com.klikli_dev.modonomicon.book.conditions.context.BookConditionEntryContext;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BookKnowledgeCondition extends BookCondition {

    public ResourceLocation advancementId;
    public int knowledge;
    public Component advancement;
    public boolean hasAdvancement;
    public BookKnowledgeCondition(Component component, int knowledge, ResourceLocation advancementId, boolean hasAdvancement) {
        super(component);
        this.tooltip = component;
        this.advancementId = advancementId;
        this.knowledge = knowledge;
        this.hasAdvancement = hasAdvancement;
    }

    public static BookKnowledgeCondition fromJson(JsonObject json) {
        ResourceLocation advancementId = new ResourceLocation("", "");
        boolean hasAdvancement = false;
        if (json.has("advancement_id")) {
            hasAdvancement = true;
            advancementId = new ResourceLocation(GsonHelper.getAsString(json, "advancement_id"));
        }
        var knowledge = 1;
        if (json.has("knowledge")) {
            knowledge = json.get("knowledge").getAsInt();
        }


        //default tooltip
        Component tooltip = Component.literal("");
        if (json.has("tooltip")) {
            tooltip = tooltipFromJson(json);
        }

        return new BookKnowledgeCondition(tooltip, knowledge, advancementId, hasAdvancement);
    }

    @Override
    public List<Component> getTooltip(BookConditionContext context) {
        List<Component> list = new ArrayList<>();
        if (!tooltip.getString().equals("")) {
            list.add(tooltip);
        }
        list.add(Component.translatable("book.runicarts.condition.knowledge.ln1", knowledge));
        list.add(Component.translatable("book.runicarts.condition.knowledge.ln2", ClientPlayerData.getPlayerKnowledge()));
        if (hasAdvancement) {
            list.add(Component.translatable("book.runicarts.condition.knowledge.ln3", Component.translatable(makeDescriptionId("advancements", advancementId) + ".title")));
        }
        return list;
    }
    public static String makeDescriptionId(String pType, @Nullable ResourceLocation pId) {
        if (pId.getNamespace().equals("minecraft")) {
            return pId == null ? pType + ".unregistered_sadface" : pType + "." + pId.getPath().replace('/', '.');
        } else {
            return pId == null ? pType + ".unregistered_sadface" : pType + "." + pId.getNamespace() + "." + pId.getPath().replace('/', '.');
        }
    }

    public static BookKnowledgeCondition fromNetwork(FriendlyByteBuf buffer) {
        var tooltip = buffer.readBoolean() ? buffer.readComponent() : null;
        var advancementId = buffer.readResourceLocation();
        var knowledge = buffer.readInt();
        var hasAdvancement = buffer.readBoolean();
        return new BookKnowledgeCondition(tooltip, knowledge, advancementId, hasAdvancement);
    }

    @Override
    public ResourceLocation getType() {
        return new ResourceLocation(RunicArts.MOD_ID, "knowledge");
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.tooltip != null);
        if (this.tooltip != null) {
            buffer.writeComponent(this.tooltip);
        }
        buffer.writeResourceLocation(this.advancementId);
        buffer.writeInt(knowledge);
        buffer.writeBoolean(hasAdvancement);
    }

    @Override
    public boolean test(BookConditionContext context, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (context instanceof BookConditionEntryContext context2) {
                AtomicReference<PlayerModData> data = new AtomicReference<>();
                player.getCapability(PlayerModDataProvider.PLAYER_MODDATA).ifPresent((data2) -> {
                    data.set(data2);
                });
                if (data.get().getUnlocked().containsKey(context.getBook().getId())) {
                    if (data.get().getUnlocked().get(context.getBook().getId()).contains(context2.getEntry().getId())) {
                        if (hasAdvancement) {
                            var advancement = serverPlayer.getServer().getAdvancements().getAdvancement(this.advancementId);
                            return advancement != null && serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}