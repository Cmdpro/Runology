package com.cmdpro.runology.config;

import com.cmdpro.runology.Runology;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class RunologyConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final RunologyConfig COMMON;

    static {
        {
            final Pair<RunologyConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(RunologyConfig::new);
            COMMON = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
    }
    public RunologyConfig(ForgeConfigSpec.Builder builder) {
        builder.push("destructivePotential");
        stabilizedShatterSpreadsValue = buildBoolean(builder, "stabilizedShatterSpreads", "all", true, "Should Stabilized Shatters spread the shatter realm?");
    }
    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }
    public static boolean stabilizedShatterSpreads = false;
    public final ForgeConfigSpec.BooleanValue stabilizedShatterSpreadsValue;
    public static void bake(ModConfig config) {
        try {
            stabilizedShatterSpreads = COMMON.stabilizedShatterSpreadsValue.get();
        } catch (Exception e) {
            Runology.LOGGER.warn("Failed to load Runology config");
            e.printStackTrace();
        }
    }
}
