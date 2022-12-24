package com.fallenreaper.createutilities.content.blocks.hand_display;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUVoxelShapes;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

public class HandDisplayBlock extends HorizontalDirectionalBlock implements ITE<HandDisplayBlockEntity> , IWrenchable {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public HandDisplayBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder
                .add(FACING).add(POWERED));
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return super.getStateForPlacement(pContext).setValue(FACING, pContext.getHorizontalDirection()
                        .getOpposite()).setValue(POWERED,
                pContext.getLevel().hasNeighborSignal(pContext.getClickedPos()));
    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
                                 BlockHitResult pHit) {

        ItemStack itemInHand = pPlayer.getItemInHand(pHand);



            if (pPlayer.isShiftKeyDown())
                return InteractionResult.PASS;
            if (pLevel.isClientSide)
                return InteractionResult.SUCCESS;


            ItemStack inHand = pPlayer.getItemInHand(pHand);
            return onTileEntityUse(pLevel, pPos, te -> {
                ItemStack heldItem = te.getHeldItem();

                if (!inHand.isEmpty() && heldItem.isEmpty()) {
                    if (!pPlayer.isCreative()) {
                        inHand.shrink(1);
                        if (inHand.isEmpty())
                            pPlayer.setItemInHand(pHand, ItemStack.EMPTY);
                    }
                    pLevel.playSound(null, pPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1, 1);
                    te.setHeldItem(ItemHandlerHelper.copyStackWithSize(inHand, 1));

                    te.notifyUpdate();
                    return InteractionResult.SUCCESS;

                } else if (!heldItem.isEmpty() && inHand.isEmpty()) {

                    pPlayer.getInventory()
                            .placeItemBackInInventory(heldItem);
                    pLevel.playSound(null, pPos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1, 1);
                    te.setHeldItem(ItemStack.EMPTY);

                    te.notifyUpdate();
                    return InteractionResult.SUCCESS;
                }

                return InteractionResult.FAIL;
            });

    }


    @Override
    public Class<HandDisplayBlockEntity> getTileEntityClass() {
        return HandDisplayBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HandDisplayBlockEntity> getTileEntityType() {
        return CUBlockEntities.HAND_DISPLAY.get();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUVoxelShapes.HAND_DISPLAY.get(Direction.NORTH);
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
       return CUVoxelShapes.HAND_DISPLAY.get(Direction.NORTH);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        if (worldIn.isClientSide)
            return;
        this.withTileEntityDo(worldIn, pos, HandDisplayBlockEntity::updateDisplayMode);
        boolean previouslyPowered = state.getValue(POWERED);
        if ( worldIn.hasNeighborSignal(pos) && !previouslyPowered) {
            state.setValue(POWERED, true);
        }
    }


}
