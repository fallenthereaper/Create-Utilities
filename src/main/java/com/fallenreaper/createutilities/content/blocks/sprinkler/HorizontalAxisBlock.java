package com.fallenreaper.createutilities.content.blocks.sprinkler;

import com.fallenreaper.createutilities.index.CUVoxelShapes;
import com.simibubi.create.content.contraptions.base.KineticBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class HorizontalAxisBlock extends KineticBlock {

    public static final BooleanProperty CEILING = BooleanProperty.create("ceiling");
    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public HorizontalAxisBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(CEILING, false));

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CEILING, HORIZONTAL_FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(CEILING, ctx.getClickedFace() == Direction.DOWN).setValue(HORIZONTAL_FACING, ctx.getHorizontalDirection()
                .getOpposite());
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pBlockPos,
                                        CollisionContext ctx) {
        return state.getValue(CEILING) ? CUVoxelShapes.SPRINKLER_CEILING.get(Direction.UP) : CUVoxelShapes.SPRINKLER.get(Direction.UP);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.X;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(HORIZONTAL_FACING, direction.rotate(state.getValue(HORIZONTAL_FACING))).setValue(CEILING, state.getValue(CEILING));
    }
}
