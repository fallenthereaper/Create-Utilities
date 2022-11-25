package com.fallenreaper.createutilities.content.items.watering_can;

import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

public class WateringCanFluidTank extends SmartFluidTank {

    protected State state;

    public WateringCanFluidTank(int capacity, Consumer<FluidStack> updateCallback) {
        super(capacity, updateCallback);
    }
    public void setState(State state) {
        this.state = state;
    }
    public State getState() {
        return state;
    }

    public enum State {
        HYDRATING,
        PAUSED;

        public boolean isHydrating() {
            return this == HYDRATING;
        }

        public boolean isPaused() {
            return this == PAUSED;
        }
    }
}
