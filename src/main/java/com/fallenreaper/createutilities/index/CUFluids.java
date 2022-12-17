package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.simibubi.create.content.contraptions.fluids.VirtualFluid;
import com.tterrag.registrate.util.entry.FluidEntry;


public class CUFluids {

    public static final FluidEntry<VirtualFluid> STEAM =
        CreateUtilities.registrate().virtualFluid("steam")
                .lang(f -> "fluid." + CreateUtilities.ID + ".steam", "Steam")
                .register();


    public static void register() {
    }

}
