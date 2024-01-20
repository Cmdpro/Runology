package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.analyzetasks.ConsumeItemTaskSerializer;
import com.cmdpro.runology.analyzetasks.GetItemTask;
import com.cmdpro.runology.analyzetasks.GetItemTaskSerializer;
import com.cmdpro.runology.api.AnalyzeTaskSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AnalyzeTaskInit {
    public static final DeferredRegister<AnalyzeTaskSerializer> ANALYZE_TASKS = DeferredRegister.create(new ResourceLocation(Runology.MOD_ID, "analyzetasks"), Runology.MOD_ID);

    public static final RegistryObject<AnalyzeTaskSerializer> GETITEM = register("getitem", () -> GetItemTaskSerializer.INSTANCE);
    public static final RegistryObject<AnalyzeTaskSerializer> CONSUMEITEM = register("consumeitem", () -> ConsumeItemTaskSerializer.INSTANCE);
    private static <T extends AnalyzeTaskSerializer> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ANALYZE_TASKS.register(name, item);
    }
}
