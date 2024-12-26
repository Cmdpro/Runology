package com.cmdpro.runology.registry;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.block.transmission.ShatteredFocusBlockEntity;
import com.cmdpro.runology.block.transmission.ShatteredRelayBlockEntity;
import com.cmdpro.runology.block.world.ShatterBlockEntity;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.function.Supplier;

public class AttachmentTypeRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
            Runology.MODID);
    public static final Supplier<AttachmentType<Integer>> SHATTER_ITEM_CONVERSION_TIMER =
            register("shatter_item_conversion_timer", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build());
    public static final Supplier<AttachmentType<Boolean>> PLAYER_POWER_MODE =
            register("player_power_mode", () -> AttachmentType.builder(() -> false).build());
    public static final Supplier<AttachmentType<Integer>> PLAYER_POWER_INVINCIBILITY =
            register("player_power_invincibility", () -> AttachmentType.builder(() -> 0).build());
    public static final Supplier<AttachmentType<ArrayList<ShatterBlockEntity>>> SHATTERS =
            register("shatters", () -> AttachmentType.builder(() -> new ArrayList<ShatterBlockEntity>()).build());
    public static final Supplier<AttachmentType<ArrayList<ShatteredRelayBlockEntity>>> SHATTERED_RELAYS =
            register("shattered_relays", () -> AttachmentType.builder(() -> new ArrayList<ShatteredRelayBlockEntity>()).build());
    public static final Supplier<AttachmentType<ArrayList<ShatteredFocusBlockEntity>>> SHATTERED_FOCUSES =
            register("shattered_focuses", () -> AttachmentType.builder(() -> new ArrayList<ShatteredFocusBlockEntity>()).build());

    private static <T extends AttachmentType<?>> Supplier<T> register(final String name, final Supplier<T> attachment) {
        return ATTACHMENT_TYPES.register(name, attachment);
    }
}