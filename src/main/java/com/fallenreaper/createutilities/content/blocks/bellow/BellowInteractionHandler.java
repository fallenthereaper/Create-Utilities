package com.fallenreaper.createutilities.content.blocks.bellow;

import com.fallenreaper.createutilities.core.utils.InteractionHandler;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BellowInteractionHandler extends InteractionHandler {

    public BellowInteractionHandler(SmartTileEntity te) {
        super(te);
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

    @Override
    public void onInteract(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {

    }

    @Override
    public void init() {

    }
}

