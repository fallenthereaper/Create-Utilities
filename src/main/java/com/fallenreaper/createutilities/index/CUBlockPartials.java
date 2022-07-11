package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.jozufozu.flywheel.core.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class CUBlockPartials {

    public static final PartialModel
            //  STEERING_WHEEL= getPartialModel("steering_wheel/steering_wheel"),
            SPRINKLER_PROPAGATOR = getPartialModel("sprinkler/leaker"),
            SCHEMATIC_MODEL = getPartialModel("typewriter/schematic"),
           // PUNCHCARD = getPartialModel("typewriter/punchcard"),
            BELLOWS = getPartialModel("bellow/bellows");

    public static PartialModel getPartialModel(String path) {
       ResourceLocation resourceLocation = CreateUtilities.defaultResourceLocation("block/" + path);
        return new PartialModel(resourceLocation);
    }

    public static void register() {}
}