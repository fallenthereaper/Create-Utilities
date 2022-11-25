package com.fallenreaper.createutilities.utils;

import com.jozufozu.flywheel.repack.joml.Vector3d;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;

public class InteractionHandler {
   protected SmartTileEntity te;
    protected BlockPos blockPos;
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
    public InteractionHandler(SmartTileEntity te, BlockPos pos) {
        this.te = te;
        this.blockPos = pos;

    }
    public static boolean isInsideCircle(double radius, BlockPos blockPos, BlockPos target) {
        Vector3d centerPos = new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        Vector3d targetPos = new Vector3d(target.getX(), target.getY(), target.getZ());
        double distance = (int) centerPos.distance(targetPos);

        return distance <= radius;

    }
    public static boolean isInsideCircle(double radius, Vector3d center, Vector3d target) {
        double distance = center.distance(target);
        return distance <= radius;
    }
}
