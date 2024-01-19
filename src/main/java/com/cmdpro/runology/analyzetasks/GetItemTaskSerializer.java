package com.cmdpro.runology.analyzetasks;

import com.cmdpro.runology.api.AnalyzeTask;
import com.cmdpro.runology.api.AnalyzeTaskSerializer;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public class GetItemTaskSerializer extends AnalyzeTaskSerializer {
    public static final GetItemTaskSerializer INSTANCE = new GetItemTaskSerializer();

    @Override
    public AnalyzeTask fromJson(JsonObject json) {
        return new GetItemTask();
    }

    @Override
    public AnalyzeTask fromNetwork(FriendlyByteBuf buffer) {
        return new GetItemTask();
    }

    @Override
    public void toNetwork(AnalyzeTask task, FriendlyByteBuf buffer) {

    }
}
