/*
package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.components.steam.SteamEngineBlock;
import com.simibubi.create.content.contraptions.components.steam.whistle.WhistleBlock;
import com.simibubi.create.content.contraptions.components.steam.whistle.WhistleTileEntity;
import com.simibubi.create.content.contraptions.fluids.tank.*;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
public class SteamFurnaceBoilerData extends BoilerData {
    static final int SAMPLE_RATE = 5;

    private static final int waterSupplyPerLevel = 10;
    private static final float passiveEngineEfficiency = 1 / 8f;

    // pooled water supply
    int gatheredSupply;
    float[] supplyOverTime = new float[10];
    int ticksUntilNextSample;
    int currentIndex;

    // heat score
    public boolean needsHeatLevelUpdate;
    public boolean passiveHeat;
    public int activeHeat;

    public float waterSupply;
    public int attachedEngines;
    public int attachedWhistles;

    // display
    private int maxHeatForSize = 0;
    private int maxHeatForWater = 0;
    private int minValue = 0;
    private int maxValue = 0;

    public LerpedFloat gauge = LerpedFloat.linear();

    public boolean evaluate(SteamFurnaceBlockEntity controller) {
        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        int prevEngines = attachedEngines;
        int prevWhistles = attachedWhistles;
        attachedEngines = 0;
        attachedWhistles = 0;


        BlockState blockState = level.getBlockState(controllerPos);

                    for (Direction d : Iterate.directions) {
                        BlockPos attachedPos = controllerPos.relative(d);
                        BlockState attachedState = level.getBlockState(attachedPos);
                        if (AllBlocks.STEAM_ENGINE.has(attachedState) && SteamEngineBlock.getFacing(attachedState) == d)
                            attachedEngines++;
                        if (AllBlocks.STEAM_WHISTLE.has(attachedState)
                                && WhistleBlock.getAttachedDirection(attachedState)
                                .getOpposite() == d)
                            attachedWhistles++;

            }


        needsHeatLevelUpdate = true;
        return prevEngines != attachedEngines || prevWhistles != attachedWhistles;
    }


    public void tick(SteamFurnaceBlockEntity controller) {

        if (!isActive())
            return;
        if (controller.getLevel().isClientSide) {
            gauge.tickChaser();
            float current = gauge.getValue(1);
            if (current > 1 && Create.RANDOM.nextFloat() < 1 / 2f)
                gauge.setValueNoUpdate(current + Math.min(-(current - 1) * Create.RANDOM.nextFloat(), 0));
            return;
        }
        if (needsHeatLevelUpdate && updateTemperature(controller))
            controller.notifyUpdate();
        ticksUntilNextSample--;
        if (ticksUntilNextSample > 0)
            return;
        int capacity = controller.getTank().getCapacity();
        if (capacity == 0)
            return;

        ticksUntilNextSample = SAMPLE_RATE;
        supplyOverTime[currentIndex] = gatheredSupply / (float) SAMPLE_RATE;
        waterSupply = Math.max(waterSupply, supplyOverTime[currentIndex]);
        currentIndex = (currentIndex + 1) % supplyOverTime.length;
        gatheredSupply = 0;

        if (currentIndex == 0) {
            waterSupply = 0;
            for (float i : supplyOverTime)
                waterSupply = Math.max(i, waterSupply);
        }

        if (controller.isCreative)
            waterSupply = waterSupplyPerLevel * 20;


        controller.notifyUpdate();

    }

    public void checkPipeOrganAdvancement(FluidTankTileEntity controller) {
        if (!controller.getBehaviour(AdvancementBehaviour.TYPE)
                .isOwnerPresent())
            return;

        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        Set<Integer> whistlePitches = new HashSet<>();

        for (int yOffset = 0; yOffset < controller.height; yOffset++) {
            for (int xOffset = 0; xOffset < controller.width; xOffset++) {
                for (int zOffset = 0; zOffset < controller.width; zOffset++) {

                    BlockPos pos = controllerPos.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!FluidTankBlock.isTank(blockState))
                        continue;
                    for (Direction d : Iterate.directions) {
                        BlockPos attachedPos = pos.relative(d);
                        BlockState attachedState = level.getBlockState(attachedPos);
                        if (AllBlocks.STEAM_WHISTLE.has(attachedState)
                                && WhistleBlock.getAttachedDirection(attachedState)
                                .getOpposite() == d) {
                            if (level.getBlockEntity(attachedPos)instanceof WhistleTileEntity wte)
                                whistlePitches.add(wte.getPitchId());
                        }
                    }
                }
            }
        }

        if (whistlePitches.size() >= 12)
            controller.award(AllAdvancements.PIPE_ORGAN);
    }

    public boolean updateTemperature(FluidTankTileEntity controller) {
        BlockPos controllerPos = controller.getBlockPos();
        Level level = controller.getLevel();
        needsHeatLevelUpdate = false;

        boolean prevPassive = passiveHeat;
        int prevActive = activeHeat;
        passiveHeat = false;
        activeHeat = 0;

        for (int xOffset = 0; xOffset < controller.width; xOffset++) {
            for (int zOffset = 0; zOffset < controller.width; zOffset++) {
                BlockPos pos = controllerPos.offset(xOffset, -1, zOffset);
                BlockState blockState = level.getBlockState(pos);
                float heat = BoilerHeaters.getActiveHeat(level, pos, blockState);
                if (heat == 0) {
                    passiveHeat = true;
                } else if (heat > 0) {
                    activeHeat += heat;
                }
            }
        }

        passiveHeat &= activeHeat == 0;

        return prevActive != activeHeat || prevPassive != passiveHeat;
    }

    public boolean isActive() {
        return attachedEngines > 0 || attachedWhistles > 0;
    }

    public void clear() {
        waterSupply = 0;
        activeHeat = 0;
        passiveHeat = false;
        attachedEngines = 0;
        Arrays.fill(supplyOverTime, 0);
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("Supply", waterSupply);
        nbt.putInt("ActiveHeat", activeHeat);
        nbt.putBoolean("PassiveHeat", passiveHeat);
        nbt.putInt("Engines", attachedEngines);
        nbt.putInt("Whistles", attachedWhistles);
        nbt.putBoolean("Update", needsHeatLevelUpdate);
        return nbt;
    }

    public void read(CompoundTag nbt, int boilerSize) {
        waterSupply = nbt.getFloat("Supply");
        activeHeat = nbt.getInt("ActiveHeat");
        passiveHeat = nbt.getBoolean("PassiveHeat");
        attachedEngines = nbt.getInt("Engines");
        attachedWhistles = nbt.getInt("Whistles");
        needsHeatLevelUpdate = nbt.getBoolean("Update");
        Arrays.fill(supplyOverTime, (int) waterSupply);

        int forBoilerSize = getMaxHeatLevelForBoilerSize(boilerSize);
        int forWaterSupply = getMaxHeatLevelForWaterSupply();
        int actualHeat = Math.min(activeHeat, Math.min(forWaterSupply, forBoilerSize));
        float target = isPassive(boilerSize) ? 1 / 8f : forBoilerSize == 0 ? 0 : actualHeat / (forBoilerSize * 1f);
        gauge.chase(target, 0.125f, LerpedFloat.Chaser.EXP);
    }

    public BoilerData.BoilerFluidHandler createHandler() {
        return new BoilerData.BoilerFluidHandler();
    }

    public class BoilerFluidHandler implements IFluidHandler {

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }

        @Override
        public int getTankCapacity(int tank) {
            return 10000;
        }

        @Override
        public boolean isFluidValid(int tank, FluidStack stack) {
            return FluidHelper.isWater(stack.getFluid());
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (!isFluidValid(0, resource))
                return 0;
            int amount = resource.getAmount();
            if (action.execute())
                gatheredSupply += amount;
            return amount;
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            return FluidStack.EMPTY;
        }

        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {
            return FluidStack.EMPTY;
        }

    }

}
 */