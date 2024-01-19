package com.cmdpro.runology.api;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

public abstract class AnalyzeTaskSerializer {
    public abstract AnalyzeTask fromJson(JsonObject json);
    public abstract AnalyzeTask fromNetwork(FriendlyByteBuf buffer);
    public abstract void toNetwork(AnalyzeTask task, FriendlyByteBuf buffer);
}
