package com.fallenreaper.createutilities.content.blocks.sprinkler;

import com.fallenreaper.createutilities.utils.InteractionHandler;
import com.jozufozu.flywheel.repack.joml.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.FarmlandWaterManager;


public class SprinklerInteractionHandler extends InteractionHandler {


    public static boolean hasFarmlandBlock(BlockPos pos, Level level) {
        BlockState stateAtPos = level.getBlockState(pos);

        Block getBlock = stateAtPos.getBlock();

        return getBlock instanceof FarmBlock;
    }

    public static void hydrateFarmland(BlockPos blockPos, Level worldIn, BlockState state, AABB aabb) {

        int getMoistureLevel = state.getValue(FarmBlock.MOISTURE);

        if (getMoistureLevel < 7) {

            worldIn.setBlock(blockPos, state.setValue(FarmBlock.MOISTURE, 7), 2);
            FarmlandWaterManager.addAABBTicket(worldIn, aabb);

        }


    }

    public static boolean isInsideCircle(double radius, BlockPos blockPos, BlockPos target) {
        Vector3d centerPos = new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        Vector3d targetPos = new Vector3d(target.getX(), target.getY(), target.getZ());
        double distance = (int) centerPos.distance(targetPos);

        return distance <= radius;

    }

    public static boolean checkForPlants(Block block) {
        return block instanceof BonemealableBlock;
    }

    public static double randomWithRange(double min, double max) {

        double range = (max - min) + 1;
        return (Math.random() * range) + min;
    }
}



