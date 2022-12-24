package com.fallenreaper.createutilities.content.blocks.encased_nixie_tube;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUVoxelShapes;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.content.logistics.block.redstone.DoubleFaceAttachedBlock;
import com.simibubi.create.content.schematics.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.ItemRequirement;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiConsumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class EncasedNixieTubeBlock  extends DoubleFaceAttachedBlock
        implements ITE<EncasedNixieTubeBlockEntity>, IWrenchable, SimpleWaterloggedBlock, ISpecialBlockItemRequirement, INixieTube
{


    public final DyeColor color;

    public EncasedNixieTubeBlock(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        registerDefaultState(defaultBlockState().setValue(FACE, DoubleAttachFace.FLOOR)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACE, FACING, WATERLOGGED));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (player.isShiftKeyDown())
            return InteractionResult.PASS;

        ItemStack heldItem = player.getItemInHand(hand);
        EncasedNixieTubeBlockEntity nixie = getTileEntity(world, pos);

        if (nixie == null)
            return InteractionResult.PASS;
        if (heldItem.isEmpty()) {
            if (nixie.reactsToRedstone())
                return InteractionResult.PASS;
            nixie.clearCustomText();
            updateDisplayedRedstoneValue(state, world, pos);
            return InteractionResult.SUCCESS;
        }

        boolean display = heldItem.getItem() == Items.NAME_TAG && heldItem.hasCustomHoverName();
        DyeColor dye = DyeColor.getColor(heldItem);

        if (!display && dye == null)
            return InteractionResult.PASS;
        if (world.isClientSide)
            return InteractionResult.SUCCESS;

        CompoundTag tag = heldItem.getTagElement("display");
        String tagElement = tag != null && tag.contains("Name", Tag.TAG_STRING) ? tag.getString("Name") : null;

        walkNixies(world, pos, (currentPos, rowPosition) -> {
            if (display)
                withTileEntityDo(world, currentPos, te -> te.displayCustomText(tagElement, rowPosition));
            if (dye != null)
                world.setBlockAndUpdate(currentPos, withColor(state, dye));
        });

        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity te) {
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, AllBlocks.ORANGE_NIXIE_TUBE.get()
                .asItem());
    }

    public static void walkNixies(LevelAccessor world, BlockPos start, BiConsumer<BlockPos, Integer> callback) {
        BlockState state = world.getBlockState(start);
        if (!(state.getBlock() instanceof EncasedNixieTubeBlock))
            return;

        BlockPos currentPos = start;
        Direction left = state.getValue(FACING)
                .getOpposite();

        if (state.getValue(FACE) == DoubleAttachFace.WALL)
            left = Direction.UP;
        if (state.getValue(FACE) == DoubleAttachFace.WALL_REVERSED)
            left = Direction.DOWN;

        Direction right = left.getOpposite();

        while (true) {
            BlockPos nextPos = currentPos.relative(left);
            if (!areNixieBlocksEqual(world.getBlockState(nextPos), state))
                break;
            currentPos = nextPos;
        }

        int index = 0;

        while (true) {
            final int rowPosition = index;
            callback.accept(currentPos, rowPosition);
            BlockPos nextPos = currentPos.relative(right);
            if (!areNixieBlocksEqual(world.getBlockState(nextPos), state))
                break;
            currentPos = nextPos;
            index++;
        }
    }


    @Override
    public void onRemove(BlockState p_196243_1_, Level p_196243_2_, BlockPos p_196243_3_, BlockState p_196243_4_,
                         boolean p_196243_5_) {
        if (!(p_196243_4_.getBlock() instanceof EncasedNixieTubeBlock))
            p_196243_2_.removeBlockEntity(p_196243_3_);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter p_185473_1_, BlockPos p_185473_2_, BlockState p_185473_3_) {
        return AllBlocks.ORANGE_NIXIE_TUBE.asStack();
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(FACING);
        switch (pState.getValue(FACE)) {
            case CEILING:
                return CUVoxelShapes.ENCASED_NIXIE_CEILING.get(facing.getClockWise()
                        .getAxis());
            case FLOOR:
                return CUVoxelShapes.ENCASED_NIXIE_TUBE.get(facing.getClockWise()
                        .getAxis());
            default:
                return CUVoxelShapes.ENCASED_NIXIE_WALL.get(facing.getOpposite());
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos,
                                       Player player) {
        if (color != DyeColor.ORANGE)
            return AllBlocks.ORANGE_NIXIE_TUBE.get()
                    .getCloneItemStack(state, target, world, pos, player);
        return super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world,
                                  BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(WATERLOGGED))
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null)
            return null;
        if (state.getValue(FACE) != DoubleAttachFace.WALL && state.getValue(FACE) != DoubleAttachFace.WALL_REVERSED)
            state = state.setValue(FACING, state.getValue(FACING)
                    .getClockWise());
        return state.setValue(WATERLOGGED, Boolean.valueOf(context.getLevel()
                .getFluidState(context.getClickedPos())
                .getType() == Fluids.WATER));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block p_220069_4_, BlockPos p_220069_5_,
                                boolean p_220069_6_) {
        if (worldIn.isClientSide)
            return;
        if (!worldIn.getBlockTicks()
                .willTickThisTick(pos, this))
            worldIn.scheduleTick(pos, this, 0);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource r) {
        updateDisplayedRedstoneValue(state, worldIn, pos);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.getBlock() == oldState.getBlock() || isMoving)
            return;
        updateDisplayedRedstoneValue(state, worldIn, pos);
    }

    private void updateDisplayedRedstoneValue(BlockState state, Level worldIn, BlockPos pos) {
        if (worldIn.isClientSide)
            return;
        withTileEntityDo(worldIn, pos, te -> {
            if (te.reactsToRedstone())
                te.updateRedstoneStrength(getPower(worldIn, pos));
        });
    }

    static boolean isValidBlock(BlockGetter world, BlockPos pos, boolean above) {
        BlockState state = world.getBlockState(pos.above(above ? 1 : -1));
        return !state.getShape(world, pos)
                .isEmpty();
    }

    private int getPower(Level worldIn, BlockPos pos) {
        int power = 0;
        for (Direction direction : Iterate.directions)
            power = Math.max(worldIn.getSignal(pos.relative(direction), direction), power);
        for (Direction direction : Iterate.directions)
            power = Math.max(worldIn.getSignal(pos.relative(direction), Direction.UP), power);
        return power;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return side != null;
    }


    @Override
    public Class<EncasedNixieTubeBlockEntity> getTileEntityClass() {
        return EncasedNixieTubeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends EncasedNixieTubeBlockEntity> getTileEntityType() {
        return CUBlockEntities.ENCASED_NIXIE_TUBE.get();
    }

    public DyeColor getColor() {
        return color;
    }

    public static boolean areNixieBlocksEqual(BlockState blockState, BlockState otherState) {
        if (!(blockState.getBlock() instanceof EncasedNixieTubeBlock))
            return false;
        if (!(otherState.getBlock() instanceof EncasedNixieTubeBlock))
            return false;
        return withColor(blockState, DyeColor.WHITE) == withColor(otherState, DyeColor.WHITE);
    }

    public static BlockState withColor(BlockState state, DyeColor color) {
        return (color == DyeColor.ORANGE ? CUBlocks.ENCASED_ORANGE_NIXIE_TUBE :  CUBlocks.ENCASED_NIXIE_TUBES.get(color))
                .getDefaultState()
                .setValue(FACING, state.getValue(FACING))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                .setValue(FACE, state.getValue(FACE));
    }

    public static DyeColor colorOf(BlockState blockState) {
        return blockState.getBlock() instanceof EncasedNixieTubeBlock ? ((EncasedNixieTubeBlock) blockState.getBlock()).getColor()
                : DyeColor.ORANGE;
    }

    public static Direction getFacing(BlockState sideState) {
        return getConnectedDirection(sideState);
    }
}
