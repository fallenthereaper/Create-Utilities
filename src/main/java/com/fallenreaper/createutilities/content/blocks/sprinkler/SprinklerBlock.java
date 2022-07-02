package com.fallenreaper.createutilities.content.blocks.sprinkler;

import com.fallenreaper.createutilities.content.blocks.steering_wheel.IRayTraceProvider;
import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUBlockShapes;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.content.contraptions.processing.EmptyingByBasin;
import com.simibubi.create.content.contraptions.wrench.WrenchItem;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SprinklerBlock extends HorizontalKineticBlock implements ITE<SprinklerBlockEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SprinklerBlock(Properties properties) {
        super(properties);
        registerDefaultState(super.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        IRayTraceProvider r = (s, w, f, g) -> System.out.checkError();

        SprinklerBlockEntity te = (SprinklerBlockEntity) tileentity;
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() instanceof BlockItem
                && !heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                .isPresent()) {
            return InteractionResult.PASS;
        }
        if (heldItem.getItem() instanceof WrenchItem) {
            return InteractionResult.PASS;
        }
//TODO, CHECK FOR POTIONS
        if (heldItem.getItem() == Items.BUCKET) {
            if (te.isLava() || te.isWater()) {
                if (!te.fluidTankBehaviour.getPrimaryHandler().isEmpty()) {
                    if (!player.isCreative())
                        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET, 1));

                    te.getContainedFluid().shrink(Math.min(te.getContainedFluid().getAmount(), 1000));
                    if (te.isLava()) {
                        player.playSound(SoundEvents.BUCKET_EMPTY_LAVA, 1f, 1f);

                    } else {
                        player.playSound(SoundEvents.BUCKET_EMPTY, 1f, 1f);
                    }
                    te.notifyUpdate();
                }
            }
            return InteractionResult.SUCCESS;
        }


        Pair<FluidStack, ItemStack> emptyItem = EmptyingByBasin.emptyItem(worldIn, heldItem, true);

        FluidStack fluidFromItem = emptyItem.getFirst();

        te.fluidTankBehaviour.getPrimaryHandler()
                .fill(fluidFromItem, IFluidHandler.FluidAction.EXECUTE);
        te.notifyUpdate();
        if (!heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent())
            return InteractionResult.PASS;
        if (te.isLava()) {
            player.playSound(SoundEvents.BUCKET_FILL_LAVA, 1f, 1f);
        }
        if (te.isWater()) {
            player.playSound(SoundEvents.BUCKET_FILL, 1f, 1f);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUBlockShapes.SPRINKLER.get(pState.getValue(HORIZONTAL_FACING));
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return super.useShapeForLightOcclusion(pState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUBlockShapes.SPRINKLER.get(Direction.NORTH);
    }


    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pState.getValue(BlockStateProperties.WATERLOGGED)) {
            pLevel.getFluidTicks().willTickThisTick(pCurrentPos, Fluids.WATER);

        }
        return pState;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pBlockState, Level world, BlockPos pos) {
        return getTileEntityOptional(world, pos).map(SprinklerBlockEntity::getComparatorOutput)
                .orElse(0);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false)
                : Fluids.EMPTY.defaultFluidState();
    }


    @Override
    public int getDirectSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        return super.getDirectSignal(pState, pLevel, pPos, pDirection);
    }


    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    protected InteractionResult tryExchange(Level worldIn, Player player, InteractionHand handIn, ItemStack heldItem,
                                            SprinklerBlockEntity te) {
        if (FluidHelper.tryEmptyItemIntoTE(worldIn, player, handIn, heldItem, te))
            return InteractionResult.SUCCESS;
        if (EmptyingByBasin.canItemBeEmptied(worldIn, heldItem))
            return InteractionResult.SUCCESS;
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel()
                .getFluidState(context.getClickedPos());
        if (context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER) {
            return super.getStateForPlacement(context).setValue(BlockStateProperties.WATERLOGGED,
                    fluidstate.getType() == Fluids.WATER);
        }

        Direction prefferedSide = getPreferredHorizontalFacing(context);
        if (prefferedSide != null)
            return defaultBlockState().setValue(HORIZONTAL_FACING, prefferedSide);
        return super.getStateForPlacement(context);
    }


    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }


    @Override
    public boolean showCapacityWithAnnotation() {
        return true;
    }


    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return super.onSneakWrenched(state, context);
    }

    @Override
    public void playRotateSound(Level world, BlockPos pos) {
        super.playRotateSound(world, pos);
    }

    @Override
    public Class<SprinklerBlockEntity> getTileEntityClass() {
        return SprinklerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SprinklerBlockEntity> getTileEntityType() {
        return CUBlockEntities.SPRINKLER.get();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return ITE.super.newBlockEntity(p_153215_, p_153216_);
    }
}
