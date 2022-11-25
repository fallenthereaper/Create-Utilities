package com.fallenreaper.createutilities.utils.data.blocks;

import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IFluidTransferProvider {
    void registerNode();

    SmartFluidTank getTank();

    FluidNode getNode();

    Level getLevel();

    BlockPos getBlockPosition();

    boolean isRemoved();

    int getTankCapacity();

    boolean hasNode();
}
