package com.cmdpro.runicarts.init;

import com.cmdpro.runicarts.RunicArts;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AttributeInit {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, RunicArts.MOD_ID);
    public static final RegistryObject<Attribute> MAXSOULS = register("maxsouls", () -> new RangedAttribute("attribute.runicarts.maxsouls", 50, 0, Float.MAX_VALUE).setSyncable(true));
    private static <T extends Attribute> RegistryObject<T> register(final String name, final Supplier<T> attribute) {
        return ATTRIBUTES.register(name, attribute);
    }
}
