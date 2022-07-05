package com.fallenreaper.createutilities.content.blocks.bellow;

import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BellowInteractionHandler {
    public enum State{
        RUNNING,
        VALID,
        NONE;

        public boolean isRunning(){
            return this == RUNNING;
        }
        public boolean isValid(){
            return this == VALID;
        }
        public boolean isInvalid(){
            return this == NONE;
        }

    }
    public static State getState(BlockState state) {
        if(state.hasProperty(AbstractFurnaceBlock.LIT)) {
            if (state.getValue(AbstractFurnaceBlock.LIT)) {
                return State.RUNNING;
            } else {
               return State.NONE;
            }
        }
        return State.VALID;
       }

    }

