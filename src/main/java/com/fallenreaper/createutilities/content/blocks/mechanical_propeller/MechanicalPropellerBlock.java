package com.fallenreaper.createutilities.content.blocks.mechanical_propeller;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUVoxelShapes;
import com.simibubi.create.content.contraptions.base.DirectionalKineticBlock;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.content.logistics.block.chute.AbstractChuteBlock;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MechanicalPropellerBlock extends DirectionalKineticBlock implements ITE<MechanicalPropellerBlockEntity>, IWrenchable {
    public MechanicalPropellerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        blockUpdate(state, worldIn, pos);
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState stateIn, LevelAccessor worldIn, BlockPos pos, int flags, int count) {
        super.updateIndirectNeighbourShapes(stateIn, worldIn, pos, flags, count);
        blockUpdate(stateIn, worldIn, pos);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState p_196243_4_, boolean p_196243_5_) {
        if (state.hasBlockEntity() && (state.getBlock() != p_196243_4_.getBlock() || !p_196243_4_.hasBlockEntity())) {
            withTileEntityDo(world, pos, MechanicalPropellerBlockEntity::updateChute);
            world.removeBlockEntity(pos);
        }
    }


    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        blockUpdate(state, worldIn, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();

        BlockState placedOn = world.getBlockState(pos.relative(face.getOpposite()));
        BlockState placedOnOpposite = world.getBlockState(pos.relative(face));
        if (AbstractChuteBlock.isChute(placedOn))
            return defaultBlockState().setValue(FACING, face.getOpposite());
        if (AbstractChuteBlock.isChute(placedOnOpposite))
            return defaultBlockState().setValue(FACING, face);

        Direction preferredFacing = getPreferredFacing(context);
        if (preferredFacing == null)
            preferredFacing = context.getNearestLookingDirection();
        return defaultBlockState().setValue(FACING, context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown() ? preferredFacing : preferredFacing.getOpposite());
    }

    protected void blockUpdate(BlockState state, LevelAccessor worldIn, BlockPos pos) {
        if (worldIn instanceof WrappedWorld)
            return;
        notifyFanTile(worldIn, pos);
    }

    protected void notifyFanTile(LevelAccessor world, BlockPos pos) {
        withTileEntityDo(world, pos, MechanicalPropellerBlockEntity::setUpdateAirFlow);
    }

    @Override
    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        blockUpdate(newState, context.getLevel(), context.getClickedPos());
        return newState;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUVoxelShapes.MECHANICAL_PROPELLER.get(pState.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUVoxelShapes.MECHANICAL_PROPELLER.get(pState.getValue(FACING));
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUVoxelShapes.MECHANICAL_PROPELLER.get(pState.getValue(FACING));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING)
                .getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }


    @Override
    public Class<MechanicalPropellerBlockEntity> getTileEntityClass() {
        return MechanicalPropellerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalPropellerBlockEntity> getTileEntityType() {
        return CUBlockEntities.MECHANICAL_PROPELLER.get();
    }

}
