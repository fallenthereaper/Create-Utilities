package com.fallenreaper.createutilities.content.blocks.bellow;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.steam_furnace.ISteamProvider;
import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.fallenreaper.createutilities.index.CUBlockShapes;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BellowBlock extends HorizontalKineticBlock implements ITE<BellowBlockEntity>, IWrenchable {

    public BellowBlock(Properties properties) {
        super(properties);
    }

    public static BlockPos getBaseBlockPos(BlockPos pos) {
        return pos.below();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return isValidPosition(worldIn, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        if (worldIn.isClientSide)
            return;

        if (!canSurvive(state, worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction prefferedSide = getPreferredHorizontalFacing(context);
        if (prefferedSide != null)
            return defaultBlockState().setValue(HORIZONTAL_FACING, prefferedSide);

        return super.getStateForPlacement(context);
    }

    protected boolean isValidPosition(BlockGetter world, BlockPos pos) {
        BlockPos baseBlockPos = getBaseBlockPos(pos);
        BlockState getState = world.getBlockState(baseBlockPos);
        BlockEntity be = world.getBlockEntity(baseBlockPos);
        Minecraft mc = Minecraft.getInstance();
        for (Block blocks : CreateUtilities.BLOCKLIST) {

            if ((getState.getBlock() instanceof AbstractFurnaceBlock) || getState.getBlock().equals(blocks) || be instanceof ISteamProvider) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING)
                .getAxis();

    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return super.onWrenched(state, context);
    }

    @Override
    public Class<BellowBlockEntity> getTileEntityClass() {
        return BellowBlockEntity.class;
    }

    public PartialModel getPartialModel() {
        return CUBlockPartials.BELLOWS;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUBlockShapes.BELLOW.get(pState.getValue(HORIZONTAL_FACING));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUBlockShapes.BELLOW.get(pState.getValue(HORIZONTAL_FACING));
    }

    @Override
    public BlockEntityType<? extends BellowBlockEntity> getTileEntityType() {
        return CUBlockEntities.BELLOW.get();
    }
}