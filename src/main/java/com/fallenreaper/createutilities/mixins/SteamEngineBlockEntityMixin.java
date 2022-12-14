package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlockEntity;
import com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBoilerData;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.content.contraptions.base.IRotate;
import com.simibubi.create.content.contraptions.components.steam.PoweredShaftTileEntity;
import com.simibubi.create.content.contraptions.components.steam.SteamEngineBlock;
import com.simibubi.create.content.contraptions.components.steam.SteamEngineTileEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.bearing.WindmillBearingTileEntity;
import com.simibubi.create.content.contraptions.fluids.tank.BoilerData;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollOptionBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;

@Mixin(SteamEngineTileEntity.class)
public abstract class SteamEngineBlockEntityMixin {


    @Shadow(remap = false)
    public WeakReference<FluidTankTileEntity> source;
    public WeakReference<SteamFurnaceBlockEntity> sourceSteam;
    @Shadow(remap = false)
    public WeakReference<PoweredShaftTileEntity> target;
    @Shadow(remap = false)
    protected ScrollOptionBehaviour<WindmillBearingTileEntity.RotationDirection> movementDirection;

    @Shadow(remap = false)
    protected abstract void spawnParticles();

    @Shadow(remap = false)
    public abstract FluidTankTileEntity getTank();

    public WeakReference<SteamFurnaceBlockEntity> getSourceSteam() {
        return sourceSteam;
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void init(BlockEntityType<?> type, BlockPos pos, BlockState state, CallbackInfo ci) {
        this.sourceSteam = new WeakReference<>(null);
    }

    @Inject(method = "spawnParticles", at = @At(value = "FIELD", target="Lcom/simibubi/create/content/contraptions/components/steam/SteamEngineTileEntity;source:Ljava/lang/ref/WeakReference;"), remap = false )
    private void playSound(CallbackInfo ci) {
        SteamEngineTileEntity te = (SteamEngineTileEntity) (Object) this;
        SteamFurnaceBlockEntity be = sourceSteam.get();
        if( te.getLevel() == null) return;

        if (be != null) {
            if (be.getBoiler() != null) {
                float volume = 3f / Math.max(2, be.boiler.attachedEngines / 2);
                float pitch = 1.18f - be.getLevel().random.nextFloat() * .25f;
                be.getLevel().playLocalSound(be.getBlockPos().getX(), be.getBlockPos().getY(), be.getBlockPos().getZ(),
                        SoundEvents.CANDLE_EXTINGUISH, SoundSource.BLOCKS, volume, pitch, false);
                AllSoundEvents.STEAM.playAt(be.getLevel(), be.getBlockPos(), volume / 16, .8f, false);
            }
        }
    }
    /**
     * @author FallenReaper
     * @reason Had to make it this way
     */
    @Overwrite(remap = false)
    public void tick() {
        SteamEngineTileEntity te = (SteamEngineTileEntity) (Object) this;
        Direction facing = SteamEngineBlock.getFacing(te.getBlockState());

        BlockEntity be = te.getLevel().getBlockEntity(te.getBlockPos().relative(facing.getOpposite()));
        if (be == null)
            return;


        if (be instanceof SteamFurnaceBlockEntity)
            act(true, te);
        if (be instanceof FluidTankTileEntity)
            act(false, te);

    }

    public SteamFurnaceBlockEntity getFurnaceTank() {
        SteamEngineTileEntity te = (SteamEngineTileEntity) (Object) this;
        SteamFurnaceBlockEntity steamTank = sourceSteam.get();
        if (steamTank == null || steamTank.isRemoved()) {
            if (steamTank != null)
                sourceSteam = new WeakReference<>(null);
            Direction facing = SteamEngineBlock.getFacing(te.getBlockState());
            BlockEntity be = te.getLevel().getBlockEntity(te.getBlockPos().relative(facing.getOpposite()));
            if (be instanceof SteamFurnaceBlockEntity tee)
                sourceSteam = new WeakReference<>(steamTank = tee);
        }
        return steamTank;
    }

    public void act(boolean steamFurnace, SteamEngineTileEntity te) {

        PoweredShaftTileEntity shaft = te.getShaft();
        SmartTileEntity tank;
        BoilerData boilerData = null;
        if (steamFurnace) {
            tank = getFurnaceTank();
            if(getFurnaceTank() != null) {
                boilerData = getFurnaceTank().boiler;
            }
        } else {
            tank = getTank();
            if(getTank() != null) {
                boilerData = getTank().boiler;
            }
        }


        Direction facing = SteamEngineBlock.getFacing(te.getBlockState());

        if (tank == null || shaft == null) {
            if (te.getLevel().isClientSide())
                return;
            if (shaft == null)
                return;
            if (!shaft.getBlockPos()
                    .subtract(te.getBlockPos())
                    .equals(shaft.enginePos))
                return;
            if (shaft.engineEfficiency == 0)
                return;

            if (te.getLevel().isLoaded(te.getBlockPos().relative(facing.getOpposite())))
                shaft.update(te.getBlockPos(), 0, 0);
            return;
        }

        boolean verticalTarget = false;
        BlockState shaftState = shaft.getBlockState();
        Direction.Axis targetAxis = Direction.Axis.X;
        if (shaftState.getBlock() instanceof IRotate ir)
            targetAxis = ir.getRotationAxis(shaftState);
        verticalTarget = targetAxis == Direction.Axis.Y;

        BlockState blockState = te.getBlockState();
        if (!AllBlocks.STEAM_ENGINE.has(blockState))
            return;

        if (facing.getAxis() == Direction.Axis.Y)
            facing = blockState.getValue(SteamEngineBlock.FACING);


        float efficiency = 0;
        if (!steamFurnace) {
            efficiency = Mth.clamp(boilerData.getEngineEfficiency(((FluidTankTileEntity) tank).getTotalTankSize()), 0, 1);
        }
        else {
            if (boilerData instanceof SteamFurnaceBoilerData data) {
                efficiency = Mth.clamp(data.getEngineEfficiency(), 0, 1);
            }
        }

        if (efficiency > 0)
            te.award(AllAdvancements.STEAM_ENGINE);

        int conveyedSpeedLevel =
                efficiency == 0 ? 1 : verticalTarget ? 1 : (int) GeneratingKineticTileEntity.convertToDirection(1, facing);
        if (targetAxis == Direction.Axis.Z)
            conveyedSpeedLevel *= -1;
        if (movementDirection.get() == WindmillBearingTileEntity.RotationDirection.COUNTER_CLOCKWISE)
            conveyedSpeedLevel *= -1;

        float shaftSpeed = shaft.getTheoreticalSpeed();
        if (shaft.hasSource() && shaftSpeed != 0 && conveyedSpeedLevel != 0
                && (shaftSpeed > 0) != (conveyedSpeedLevel > 0)) {
            movementDirection.setValue(1 - movementDirection.get()
                    .ordinal());
            conveyedSpeedLevel *= -1;
        }

        shaft.update(te.getBlockPos(), conveyedSpeedLevel, efficiency);

        if (!te.getLevel().isClientSide)
            return;

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::spawnParticles);
    }
}
