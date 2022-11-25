package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUBlockShapes;
import com.fallenreaper.createutilities.utils.data.blocks.InteractableBlockEntity;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SteamFurnaceBlock extends HorizontalKineticBlock implements ITE<SteamFurnaceBlockEntity>, IWrenchable {
    public SteamFurnaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }
    @Override
    public Class<SteamFurnaceBlockEntity> getTileEntityClass() {
        return SteamFurnaceBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SteamFurnaceBlockEntity> getTileEntityType() {
        return CUBlockEntities.STEAM_FURNACE.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof InteractableBlockEntity be) {
            return be.onInteract(state, world, pos, player, hand, ray);
        }
        return super.use(state, world, pos, player, hand, ray);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CUBlockShapes.STEAM_FURNACE.get(pState.getValue(HORIZONTAL_FACING));
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return CUBlockShapes.STEAM_FURNACE.get(state.getValue(HORIZONTAL_FACING));
    }
}
