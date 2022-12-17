package com.fallenreaper.createutilities.core.data.blocks.liquidtank;

import com.fallenreaper.createutilities.core.data.blocks.BaseInteractableBlock;
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
    public BlockEntityType<? extends LiquidTankBlockEntity> getTileEntityType() {
        return CUBlockEntities.LIQUID_TANK.get();
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        //remove from saved

    }
}
