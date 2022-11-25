package com.fallenreaper.createutilities.content.blocks.sprinkler;

import com.fallenreaper.createutilities.utils.InteractionHandler;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.FarmlandWaterManager;

//TODO: move all hydrate stuff to here
public class SprinklerInteractionHandler extends InteractionHandler {


    public SprinklerInteractionHandler(SmartTileEntity te, BlockPos pos) {
        super(te, pos);
    }

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

    public static boolean checkForPlants(Block block) {
        return block instanceof BonemealableBlock;
    }

    public static double randomWithRange(double min, double max) {

        double range = (max - min) + 1;
        return (Math.random() * range) + min;
    }
}



