package com.fallenreaper.createutilities.content.blocks.sprinkler;

import net.minecraftforge.fluids.FluidStack;

public interface IContainerHelper<T extends FluidStack> {
    boolean isWater();

    boolean isLava();

    boolean isLoaded();

    T getContainedFluid();



}
