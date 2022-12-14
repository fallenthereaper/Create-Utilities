package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.fallenreaper.createutilities.core.utils.InteractionHandler;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TypewriterInteractionHandler extends InteractionHandler {

    public TypewriterInteractionHandler(SmartTileEntity te) {
        super(te);
    }

    @Override
    public void onInteract(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {

    }

    @Override
    public void init() {

    }
}
