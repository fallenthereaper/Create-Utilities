package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import net.minecraftforge.common.ForgeConfigSpec;

public class CUConfig {
    public static final ForgeConfigSpec SERVER_CONFIG;
    public static final String SERVER_FILENAME = CreateUtilities.ID + "-server.toml";
    public static final String MAIN = "General";
    public static final ForgeConfigSpec.IntValue FARMLAND_HYDRATE_CHANCE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push(MAIN);

        FARMLAND_HYDRATE_CHANCE = builder
                .comment("Chance of occurring a random tick inside the radius")
                .defineInRange("hydrationChance", 5, 0, 25);

        builder.pop();

        SERVER_CONFIG = builder.build();
    }
}
