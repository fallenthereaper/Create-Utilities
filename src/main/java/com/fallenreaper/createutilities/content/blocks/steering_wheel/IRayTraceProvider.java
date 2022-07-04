package com.fallenreaper.createutilities.content.blocks.steering_wheel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@FunctionalInterface
public interface IRayTraceProvider {

    boolean  getRayTrace(BlockHitResult blockRayTraceResult, BlockPos pos, Level levelIn, BlockState state);

}


