package com.fallenreaper.createutilities.core.utils;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class InteractionHandler {
   protected SmartTileEntity te;
   protected BlockPos blockPos;

    public void tick() {
    }

    public abstract void onInteract(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult);


    public abstract void init();


    public void read(CompoundTag compoundTag, boolean clientPacket) {

    }

    public void write(CompoundTag compoundTag, boolean clientPacket) {

    }

    public InteractionHandler(SmartTileEntity te) {
        this.te = te;
        this.blockPos = te.getBlockPos();
        init();

    }


    public enum State {
        RUNNING,
        VALID,
        PAUSED,
        NONE;

        public boolean isRunning() {
            return this == RUNNING;
        }

        public boolean isPaused() {
            return this == PAUSED;
        }

        public boolean isValid() {
            return this == VALID;
        }

        public boolean isInvalid() {
            return this == NONE;
        }
    }
}
