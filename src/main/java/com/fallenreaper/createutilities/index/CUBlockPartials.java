package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.jozufozu.flywheel.core.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class CUBlockPartials {

    public static final PartialModel
          //  STEERING_WHEEL= getPartialModel("steering_wheel/steering_wheel"),
            SPRINKLER_PROPAGATOR= getPartialModel("sprinkler/leaker");

    public static PartialModel getPartialModel(String path) {
        ResourceLocation L = new ResourceLocation(CreateUtilities.ID, "block/" + path);
        return new PartialModel(L);
    }
    public static void clientInit() {
        // init static fields
    }
}
