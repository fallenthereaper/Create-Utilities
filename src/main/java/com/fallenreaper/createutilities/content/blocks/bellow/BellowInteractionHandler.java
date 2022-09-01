package com.fallenreaper.createutilities.content.blocks.bellow;

import com.fallenreaper.createutilities.utils.InteractionHandler;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BellowInteractionHandler extends InteractionHandler {

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

