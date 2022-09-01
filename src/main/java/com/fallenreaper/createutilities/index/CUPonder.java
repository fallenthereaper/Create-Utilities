package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.ponder.CUPonderScenes;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;

public class CUPonder {

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CreateUtilities.ID);

    public static void register() {

        HELPER.addStoryBoard(CUBlocks.SPRINKLER, "sprinkler", CUPonderScenes::sprinkler, PonderTag.KINETIC_APPLIANCES, PonderTag.KINETIC_APPLIANCES);

        PonderRegistry.TAGS.forTag(PonderTag.KINETIC_APPLIANCES)
                .add(CUBlocks.SPRINKLER);


    }
}
