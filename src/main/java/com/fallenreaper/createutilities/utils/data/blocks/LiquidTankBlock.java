package com.fallenreaper.createutilities.utils.data.blocks;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LiquidTankBlock extends BaseInteractableBlock implements ITE<LiquidTankBlockEntity> {
//add redstone power
    public LiquidTankBlock(Properties pProperties) {
        super(pProperties);
    }



    @Override
    public Class<LiquidTankBlockEntity> getTileEntityClass() {
        return LiquidTankBlockEntity.class;
    }
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        withTileEntityDo(world, pos, LiquidTankBlockEntity::onAdded);
        if (p_220082_5_)
            return;

    }

    @Override
    public BlockEntityType<? extends LiquidTankBlockEntity> getTileEntityType() {
        return CUBlockEntities.LIQUID_TANK.get();
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        //remove from saved

    }
}
