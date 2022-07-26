package com.fallenreaper.createutilities.content.blocks.sliding_door;

import com.simibubi.create.content.curiosities.deco.SlidingDoorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ModifiedSlidingDoorBlockEntity extends SlidingDoorTileEntity {
    public ModifiedSlidingDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
