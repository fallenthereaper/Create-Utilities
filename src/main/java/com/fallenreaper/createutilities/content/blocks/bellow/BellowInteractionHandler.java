package com.fallenreaper.createutilities.content.blocks.bellow;

import com.fallenreaper.createutilities.utils.InteractionHandler;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BellowInteractionHandler extends InteractionHandler {

    public BellowInteractionHandler(SmartTileEntity te, BlockPos pos) {
        super(te, pos);
    }

    public static State getState(BlockState state) {
        if (state.hasProperty(AbstractFurnaceBlock.LIT)) {
            if (state.getValue(AbstractFurnaceBlock.LIT)) {
                return State.RUNNING;
            } else {
                return State.NONE;
            }
        }
        return State.VALID;
    }

}

