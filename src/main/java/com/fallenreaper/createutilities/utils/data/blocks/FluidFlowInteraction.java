package com.fallenreaper.createutilities.utils.data.blocks;

import com.fallenreaper.createutilities.utils.InteractionHandler;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FluidFlowInteraction extends InteractionHandler {
    protected FluidNode fluidNode;
    public FluidFlowInteraction(SmartTileEntity te, FluidNode fluidNode) {
        super(te);
        this.fluidNode = fluidNode;
    }

    @Override
    public void onInteract(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {

    }

    @Override
    public void init() {

    }

    @Override
    public void tick() {
        fluidNode.tick();
    }

}
