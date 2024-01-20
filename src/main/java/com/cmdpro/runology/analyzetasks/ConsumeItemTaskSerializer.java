package com.cmdpro.runology.analyzetasks;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.api.AnalyzeTask;
import com.cmdpro.runology.api.AnalyzeTaskSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsumeItemTaskSerializer extends AnalyzeTaskSerializer<ConsumeItemTask> {
    public static final ConsumeItemTaskSerializer INSTANCE = new ConsumeItemTaskSerializer();

    @Override
    public AnalyzeTask fromJson(JsonObject json) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (JsonElement i : json.get("items").getAsJsonArray()) {
            Runology.LOGGER.info(i.getAsJsonObject().toString());
            items.add(itemStackFromJson(i.getAsJsonObject()));
            Runology.LOGGER.info("" + itemStackFromJson(i.getAsJsonObject()).getCount());
        }
        return new ConsumeItemTask(items.toArray(new ItemStack[0]));
    }
    public static ItemStack itemStackFromJson(JsonObject pStackObject) {
        return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(pStackObject, true, true);
    }

    @Override
    public AnalyzeTask fromNetwork(FriendlyByteBuf buffer) {
        List<ItemStack> items = buffer.readList(FriendlyByteBuf::readItem);
        return new ConsumeItemTask(items.toArray(new ItemStack[0]));
    }

    @Override
    public void toNetwork(ConsumeItemTask task, FriendlyByteBuf buffer) {
        buffer.writeCollection(Arrays.stream(task.items).toList(), FriendlyByteBuf::writeItem);
    }
}
