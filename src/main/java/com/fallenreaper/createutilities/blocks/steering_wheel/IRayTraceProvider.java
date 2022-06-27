package com.fallenreaper.createutilities.blocks.steering_wheel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IRayTraceProvider {

   boolean getRayTrace(BlockHitResult blockRayTraceResult, BlockPos pos, Level levelIn, BlockState state);

}

