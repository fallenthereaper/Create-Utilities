package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.content.blocks.steam_furnace.ISteamProvider;
import com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.steam.PoweredShaftBlock;
import com.simibubi.create.content.contraptions.components.steam.SteamEngineBlock;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.simibubi.create.content.contraptions.components.steam.SteamEngineBlock.*;

@Mixin(SteamEngineBlock.class)
public class SteamEngineBlockMixin {
    /**
     * @author FallenReaper
     * @reason r
     */
    @Overwrite
    public static boolean canAttach(LevelReader pReader, BlockPos pPos, Direction pDirection) {
        BlockPos blockpos = pPos.relative(pDirection);
        BlockEntity blockEntity = pReader.getBlockEntity(blockpos);
        Block block = pReader.getBlockState(blockpos)
                .getBlock();
        return block instanceof FluidTankBlock || block instanceof SteamFurnaceBlock || blockEntity instanceof ISteamProvider;
    }

    /**
     * @author FallenReaper
     * @reason r
     */
    @Overwrite
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        Block block = pLevel.getBlockState(pPos.relative(getFacing(pState).getOpposite())).getBlock();
        if(block instanceof FluidTankBlock) {
            FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getFacing(pState).getOpposite()));
        }
        if(block instanceof SteamFurnaceBlock) {
            SteamFurnaceBlock.updateBoilerState(pState, pLevel, pPos.relative(getFacing(pState).getOpposite()));
        }
        BlockPos shaftPos = getShaftPos(pState, pPos);
        BlockState shaftState = pLevel.getBlockState(shaftPos);
        if (isShaftValid(pState, shaftState))
            pLevel.setBlock(shaftPos, PoweredShaftBlock.getEquivalent(shaftState), 3);
    }

    /**
     * @author FallenReaper
     * @reason r
     */
    @Overwrite
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        Block block = pLevel.getBlockState(pPos.relative(getFacing(pState).getOpposite())).getBlock();
        if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity()))
            pLevel.removeBlockEntity(pPos);
        if(block instanceof FluidTankBlock) {
            FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getFacing(pState).getOpposite()));
        }
        if(block instanceof SteamFurnaceBlock) {
            SteamFurnaceBlock.updateBoilerState(pState, pLevel, pPos.relative(getFacing(pState).getOpposite()));
        }
        BlockPos shaftPos = getShaftPos(pState, pPos);
        BlockState shaftState = pLevel.getBlockState(shaftPos);
        if (AllBlocks.POWERED_SHAFT.has(shaftState))
            pLevel.scheduleTick(shaftPos, shaftState.getBlock(), 1);
    }
}
