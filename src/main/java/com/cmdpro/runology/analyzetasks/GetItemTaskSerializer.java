package com.cmdpro.runology.analyzetasks;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.AnalyzeTask;
import com.cmdpro.runology.api.AnalyzeTaskSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetItemTaskSerializer extends AnalyzeTaskSerializer<GetItemTask> {
    public static final GetItemTaskSerializer INSTANCE = new GetItemTaskSerializer();

    @Override
    public AnalyzeTask fromJson(JsonObject json) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (JsonElement i : json.get("items").getAsJsonArray()) {
            Runology.LOGGER.info(i.getAsJsonObject().toString());
            items.add(itemStackFromJson(i.getAsJsonObject()));
            Runology.LOGGER.info("" + itemStackFromJson(i.getAsJsonObject()).getCount());
        }
        return new GetItemTask(items.toArray(new ItemStack[0]));
    }
    public static ItemStack itemStackFromJson(JsonObject pStackObject) {
        return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(pStackObject, true, true);
    }

    @Override
    public AnalyzeTask fromNetwork(FriendlyByteBuf buffer) {
        List<ItemStack> items = buffer.readList(FriendlyByteBuf::readItem);
        return new GetItemTask(items.toArray(new ItemStack[0]));
    }

    @Override
    public void toNetwork(GetItemTask task, FriendlyByteBuf buffer) {
        buffer.writeCollection(Arrays.stream(task.items).toList(), FriendlyByteBuf::writeItem);
    }
}
