package com.fallenreaper.createutilities.blocks.steering_wheel;

import com.fallenreaper.createutilities.index.CUBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/*
public class SteeringWheelBlockEntity extends GeneratingKineticTileEntity {

    public int inUse;
    public boolean backwards;
    public float independentAngle;
    public float chasingVelocity;



    public void turn(boolean back) {
        boolean update = false;

        if (getGeneratedSpeed() == 0 || back != backwards)
            update = true;

        inUse = 10;
        this.backwards = back;
        if (update && !level.isClientSide)
            updateGeneratedRotation();
    }

    @Override
    public void updateFromNetwork(float maxStress, float currentStress, int networkSize) {
        super.updateFromNetwork(maxStress, currentStress, networkSize);
    }

    @Override
    public float getSpeed() {
        return super.getSpeed();
    }

    @Override
    public void attachKinetics() {
        super.attachKinetics();
    }

    @Override
    public void detachKinetics() {
        super.detachKinetics();
    }

    @Override
    public void clearKineticInformation() {
        super.clearKineticInformation();
    }

    @Override
    public float getGeneratedSpeed() {
        Block block = getBlockState().getBlock();
        if (!(block instanceof SteeringWheelBlock))
            return 0;
        SteeringWheelBlock crank = (SteeringWheelBlock) block;
        float speed = (inUse == 0 ? 0 : backwards ? -1 : 1) * crank.getRotationSpeed();
        return convertToDirection(speed, getBlockState().getValue(SteeringWheelBlock.FACING));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("InUse", inUse);
        super.write(compound, clientPacket);
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
    }

    @Override
    public boolean needsSpeedUpdate() {
        return super.needsSpeedUpdate();
    }

    @Override
    public float propagateRotationTo(KineticTileEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        return super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
    }

    @Override
    public void sendData() {
        super.sendData();
    }

    @Override
    public void causeBlockUpdate() {
        super.causeBlockUpdate();
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        inUse = compound.getInt("InUse");
        super.read(compound, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();

        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;

        if (inUse > 0) {
            inUse--;

            if (inUse == 0 && !level.isClientSide)
                updateGeneratedRotation();
        }
    }

    public SteeringWheelBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);

    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();
        if (inUse > 0 && AnimationTickHolder.getTicks() % 10 == 0) {
            if (!CUBlocks.STEERING_WHEEL.has(getBlockState()))
                return;
            AllSoundEvents.CRANKING.playAt(level, worldPosition, (inUse) / 2.5f, .65f + (10 - inUse) / 10f, true);
        }
    }

}
*/