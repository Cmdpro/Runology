package com.cmdpro.runicarts.config;

import com.cmdpro.runicarts.RunicArts;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class RunicArtsConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final RunicArtsConfig COMMON;

    static {
        {
            final Pair<RunicArtsConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(RunicArtsConfig::new);
            COMMON = specPair.getLeft();
            COMMON_SPEC = specPair.getRight();
        }
    }
    public RunicArtsConfig(ForgeConfigSpec.Builder builder) {
        //otherModAdvancementsAllowedValue = buildBoolean(builder, "otherModAdvancementsAllowed", "all", false, "Should advancements from other mods give knowledge?");
    }
    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }
    //public static boolean otherModAdvancementsAllowed = false;
    //public final ForgeConfigSpec.BooleanValue otherModAdvancementsAllowedValue;
    public static void bake(ModConfig config) {
        try {
            //otherModAdvancementsAllowed = COMMON.otherModAdvancementsAllowedValue.get();
        } catch (Exception e) {
            RunicArts.LOGGER.warn("Failed to load RunicArts config");
            e.printStackTrace();
        }
    }
}
