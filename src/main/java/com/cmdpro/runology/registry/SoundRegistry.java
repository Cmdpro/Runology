package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Runology.MODID);

    //Items
    public static final Holder<SoundEvent> RUNIC_CHISEL_USE = createBasicSound("item.runic_chisel.use");
    public static final Holder<SoundEvent> BLINK_TELEPORT = createBasicSound("item.blink_boots.teleport");

    //Blocks
    public static final Holder<SoundEvent> HEAT_FOCUS_WORKING = createBasicSound("block.heat_focus.working");
    public static final Holder<SoundEvent> HEAT_FOCUS_FINISH = createBasicSound("block.heat_focus.finish");

    //Misc
    public static final Holder<SoundEvent> SHATTERED_FLOW_NETWORK_SURGE_START = createBasicSound("shattered_flow_network.surge.start");
    public static final Holder<SoundEvent> SHATTERED_FLOW_NETWORK_SURGE_END = createBasicSound("shattered_flow_network.surge.end");

    public static Holder<SoundEvent> createBasicSound(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(Runology.locate(name)));
    }
}
