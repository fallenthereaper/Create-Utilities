package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUBlockShapes;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class TypewriterBlock extends HorizontalKineticBlock implements ITE<TypewriterBlockEntity> {

    public TypewriterBlock(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public Class<TypewriterBlockEntity> getTileEntityClass() {
        return TypewriterBlockEntity.class;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {




        ItemStack stack = player.getItemInHand(handIn);
        if (!worldIn.isClientSide && worldIn.getBlockEntity(pos) instanceof final TypewriterBlockEntity be && !stack.is(AllItems.CRAFTING_BLUEPRINT.get())) {
            withTileEntityDo(worldIn, pos,
                    typewriter -> NetworkHooks.openGui((ServerPlayer) player, typewriter, typewriter::sendToContainer));
        }

        /*
        if(!te.hasBlueprintIn()) {
            if(stack.getItem() == AllItems.CRAFTING_BLUEPRINT.get()) {
                if(te.getLevel().isClientSide)
                    return InteractionResult.SUCCESS;
                player.playSound(SoundEvents.COMPOSTER_FILL, 1f, 1f);
                te.inventory.insertItem(0, stack.split(1), false);
            }
        } else {
            if(stack.isEmpty()) {
                ItemStack stack1 = te.inventory.extractItem(0, 1, false);
                System.out.println(stack1.getItem());
                player.setItemInHand(handIn, stack1);
                return InteractionResult.CONSUME;
            }
            }
         */




      /*  if(stack.getItem() == AllItems.CRAFTING_BLUEPRINT.get() && worldIn.isClientSide()) {
           /* withTileEntityDo(worldIn, pos,
                    typewriter -> NetworkHooks.openGui((ServerPlayer) player, typewriter, typewriter::sendToContainer));
            */
        /*
            if(!te.hasBlueprintIn()) {
                    te.inventory.insertItem(0, stack, false);
            }
            return InteractionResult.SUCCESS;
        }
        */

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntityType<? extends TypewriterBlockEntity> getTileEntityType() {
        return CUBlockEntities.TYPEWRITER.get();
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUBlockShapes.TYPEWRITER.get(pState.getValue(HORIZONTAL_FACING));
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof TypewriterBlockEntity te) {
                if (pLevel instanceof ServerLevel) {

                    ContainerUtil.dropContents(pLevel, pPos, te.inventory);
                    pLevel.removeBlockEntity(pPos);
                }
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return CUBlockShapes.TYPEWRITER.get(state.getValue(HORIZONTAL_FACING));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return super.mirror(pState, pMirror);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return ITE.super.newBlockEntity(p_153215_, p_153216_);
    }

}