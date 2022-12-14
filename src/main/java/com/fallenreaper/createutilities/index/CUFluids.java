package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.simibubi.create.content.contraptions.fluids.VirtualFluid;
import com.simibubi.create.repack.registrate.util.entry.FluidEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;


public class CUFluids {

    public static final FluidEntry<VirtualFluid> STEAM =
        CreateUtilities.registrate().virtualFluid("steam")
                .lang(f -> "fluid." + CreateUtilities.ID + ".steam", "Steam")
                .register();

    private static class SteamFluidAttributes extends FluidAttributes {

        protected SteamFluidAttributes(Builder builder, Fluid fluid) {
            super(builder, fluid);
        }

        @Override
        public int getColor(BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

    }
    public static void register() {
    }

}
