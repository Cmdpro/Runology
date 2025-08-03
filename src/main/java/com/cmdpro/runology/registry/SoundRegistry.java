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
    public static final Holder<SoundEvent> OTHERWORLD_BOMB_THROW = createBasicSound("item.otherworld_bomb.throw");

    //Blocks
    public static final Holder<SoundEvent> HEAT_FOCUS_WORKING = createBasicSound("block.heat_focus.working");
    public static final Holder<SoundEvent> HEAT_FOCUS_FINISH = createBasicSound("block.heat_focus.finish");
    public static final Holder<SoundEvent> SHATTER_UNSTABLE = createBasicSound("block.shatter.unstable");
    public static final Holder<SoundEvent> SHATTER_EXPLODE = createBasicSound("block.shatter.explode");
    public static final Holder<SoundEvent> OTHERWORLDLY_ENERGY_INFUSE = createBasicSound("block.otherworldly_energy.infuse");

    //Misc
    public static final Holder<SoundEvent> SHATTERED_FLOW_NETWORK_SURGE_START = createBasicSound("shattered_flow_network.surge.start");
    public static final Holder<SoundEvent> SHATTERED_FLOW_NETWORK_SURGE_END = createBasicSound("shattered_flow_network.surge.end");

    public static Holder<SoundEvent> createBasicSound(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(Runology.locate(name)));
    }
}
