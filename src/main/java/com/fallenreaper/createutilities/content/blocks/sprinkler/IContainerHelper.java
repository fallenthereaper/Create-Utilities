package com.fallenreaper.createutilities.content.blocks.sprinkler;

import net.minecraftforge.fluids.FluidStack;

//todo, delete this class as it is completely unnecessary
public interface IContainerHelper<T extends FluidStack> {
    boolean isWater();

    boolean isLava();

    boolean isLoaded();

    T getContainedFluid();


}
