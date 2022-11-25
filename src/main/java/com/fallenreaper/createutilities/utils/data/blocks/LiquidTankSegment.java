package com.fallenreaper.createutilities.utils.data.blocks;

import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class LiquidTankSegment  {
    protected SmartFluidTank tank;
    protected LerpedFloat fluidLevel;
    protected FluidStack renderedFluid;
    protected LiquidTankBlockEntity blockEntity;
    protected Level level;

    public LiquidTankSegment(int capacity, LiquidTankBlockEntity te) {
        tank = new SmartFluidTank(capacity, f -> onFluidStackChanged());
        fluidLevel = LerpedFloat.linear()
                .startWithValue(0)
                .chase(0, .25, LerpedFloat.Chaser.EXP);
        renderedFluid = FluidStack.EMPTY;
        this.blockEntity = te;
    }

    public SmartFluidTank getTank() {
        return tank;
    }

    public void onFluidStackChanged() {
        if (!blockEntity.hasLevel())
            return;
        fluidLevel.chase(tank.getFluidAmount() / (float) tank.getCapacity(), .25, LerpedFloat.Chaser.EXP);
      //  if (!getLevel().isClientSide)
    //        sendDataLazily();
        if (blockEntity.isVirtual() && !tank.getFluid()
                .isEmpty())
            renderedFluid = tank.getFluid();
    }

    public FluidStack getRenderedFluid() {
        return renderedFluid;
    }

    public LerpedFloat getFluidLevel() {
        return fluidLevel;
    }

    public float getTotalUnits(float partialTicks) {
        return fluidLevel.getValue(partialTicks) * tank.getCapacity();
    }

    public CompoundTag writeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.put("TankContent", tank.writeToNBT(new CompoundTag()));
        compound.put("Level", fluidLevel.writeNBT());
        return compound;
    }
    public Level getLevel() {
        return blockEntity.getLevel();
    }

    public void readNBT(CompoundTag compound, boolean clientPacket) {
        tank.readFromNBT(compound.getCompound("TankContent"));
        fluidLevel.readNBT(compound.getCompound("Level"), clientPacket);
        if (!tank.getFluid()
                .isEmpty())
            renderedFluid = tank.getFluid();
    }

    public boolean isEmpty(float partialTicks) {
        FluidStack renderedFluid = getRenderedFluid();
        if (renderedFluid.isEmpty())
            return true;
        float units = getTotalUnits(partialTicks);
        if (units < 1)
            return true;
        return false;
    }

}

