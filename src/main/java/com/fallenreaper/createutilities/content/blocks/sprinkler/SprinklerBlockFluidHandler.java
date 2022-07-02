package com.fallenreaper.createutilities.content.blocks.sprinkler;

import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public class SprinklerBlockFluidHandler extends SmartFluidTank {

    public SprinklerBlockFluidHandler(int capacity, Consumer<FluidStack> updateCallback) {
        super(capacity, updateCallback);
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
    }

    @Override
    public void setFluid(FluidStack stack) {
        super.setFluid(stack);
    }
}


