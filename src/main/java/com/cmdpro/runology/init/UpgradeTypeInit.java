package com.cmdpro.runology.init;

import com.cmdpro.runology.Runology;
import com.cmdpro.runology.analyzetasks.ConsumeItemTask;
import com.cmdpro.runology.analyzetasks.GetItemTask;
import com.cmdpro.runology.api.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Math;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UpgradeTypeInit {
    public static final DeferredRegister<SpellcastingUpgrade> UPGRADES = DeferredRegister.create(new ResourceLocation(Runology.MOD_ID, "spellcastingupgrades"), Runology.MOD_ID);

    public static final RegistryObject<SpellcastingUpgrade> FIREUPGRADE = register("fire", () -> new SpellcastingUpgrade() {
        @Override
        public void tick(Player player, ItemStack stack) {
            super.tick(player, stack);
            String id = RunicEnergyInit.FIRE.getId().toString();
            CompoundTag tag = stack.getOrCreateTag();
            float max = 100;
            if (stack.getItem() instanceof Gauntlet gauntlet) {
                max = gauntlet.maxRunicEnergy;
            }
            if (stack.getItem() instanceof Staff staff) {
                max = staff.maxRunicEnergy;
            }
            CompoundTag tag2 = tag.contains("runicEnergy") ? (CompoundTag) tag.get("runicEnergy") : new CompoundTag();
            tag2.putFloat(id, Math.clamp(0, max, tag2.contains(id) ? tag2.getFloat(id)+0.2f : 0.2f));
            tag.put("runicEnergy", tag2);
        }
    });
    public static final RegistryObject<SpellcastingUpgrade> EARTHUPGRADE = register("earth", () -> new SpellcastingUpgrade() {
        @Override
        public void tick(Player player, ItemStack stack) {
            super.tick(player, stack);
            String id = RunicEnergyInit.EARTH.getId().toString();
            CompoundTag tag = stack.getOrCreateTag();
            float max = 100;
            if (stack.getItem() instanceof Gauntlet gauntlet) {
                max = gauntlet.maxRunicEnergy;
            }
            if (stack.getItem() instanceof Staff staff) {
                max = staff.maxRunicEnergy;
            }
            CompoundTag tag2 = tag.contains("runicEnergy") ? (CompoundTag) tag.get("runicEnergy") : new CompoundTag();
            tag2.putFloat(id, Math.clamp(0, max, tag2.contains(id) ? tag2.getFloat(id)+0.2f : 0.2f));
            tag.put("runicEnergy", tag2);
        }
    });
    public static final RegistryObject<SpellcastingUpgrade> AIRUPGRADE = register("air", () -> new SpellcastingUpgrade() {
        @Override
        public void tick(Player player, ItemStack stack) {
            super.tick(player, stack);
            String id = RunicEnergyInit.AIR.getId().toString();
            CompoundTag tag = stack.getOrCreateTag();
            float max = 100;
            if (stack.getItem() instanceof Gauntlet gauntlet) {
                max = gauntlet.maxRunicEnergy;
            }
            if (stack.getItem() instanceof Staff staff) {
                max = staff.maxRunicEnergy;
            }
            CompoundTag tag2 = tag.contains("runicEnergy") ? (CompoundTag) tag.get("runicEnergy") : new CompoundTag();
            tag2.putFloat(id, Math.clamp(0, max, tag2.contains(id) ? tag2.getFloat(id)+0.2f : 0.2f));
            tag.put("runicEnergy", tag2);
        }
    });
    public static final RegistryObject<SpellcastingUpgrade> WATERUPGRADE = register("water", () -> new SpellcastingUpgrade() {

        @Override
        public void tick(Player player, ItemStack stack) {
            super.tick(player, stack);
            String id = RunicEnergyInit.WATER.getId().toString();
            CompoundTag tag = stack.getOrCreateTag();
            float max = 100;
            if (stack.getItem() instanceof Gauntlet gauntlet) {
                max = gauntlet.maxRunicEnergy;
            }
            if (stack.getItem() instanceof Staff staff) {
                max = staff.maxRunicEnergy;
            }
            CompoundTag tag2 = tag.contains("runicEnergy") ? (CompoundTag) tag.get("runicEnergy") : new CompoundTag();
            tag2.putFloat(id, Math.clamp(0, max, tag2.contains(id) ? tag2.getFloat(id)+0.2f : 0.2f));
            tag.put("runicEnergy", tag2);
        }
    });
    public static final RegistryObject<SpellcastingUpgrade> TRANSFORMATIONUPGRADE = register("transformation", () -> new SpellcastingUpgrade() {

        @Override
        public HashMap<ResourceLocation, Float> costChanges(HashMap<ResourceLocation, Float> cost) {
            for (Map.Entry<ResourceLocation, Float> i : cost.entrySet()) {
                RunicEnergyType type = RunologyUtil.RUNIC_ENERGY_TYPES_REGISTRY.get().getValue(i.getKey());
                if (type.core != null) {
                    cost.put(type.core, cost.containsKey(type.core) ? cost.get(type.core) + i.getValue() : i.getValue());
                    cost.remove(i.getKey());
                }
            }
            return cost;
        }
    });
    private static <T extends SpellcastingUpgrade> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return UPGRADES.register(name, item);
    }
}
