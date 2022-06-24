package com.fallenreaper.createutilities.grouptabs;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CUItemTab {
    public static CreativeModeTab MAIN_GROUP = new CreativeModeTab("main_group") {
        @Override
        public ItemStack makeIcon() {
            return CUBlocks.SPRINKLER.asStack();
        }
    };

    // Tell Registrate to create a lang entry for the item groups
    private static final CreateRegistrate REGISTRATE = CreateUtilities.registrate().creativeModeTab(() -> MAIN_GROUP, "Create Utilities");
}
