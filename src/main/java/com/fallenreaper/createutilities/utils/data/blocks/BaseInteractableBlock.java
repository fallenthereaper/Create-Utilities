package com.fallenreaper.createutilities.utils.data.blocks;

import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BaseInteractableBlock extends Block {
    protected VoxelShape baseVoxelShape;

    public BaseInteractableBlock(Properties pProperties) {
        super(pProperties);
    }

    public BaseInteractableBlock setBaseShape(VoxelShape baseVoxelShape) {
        this.baseVoxelShape = baseVoxelShape;
        return this;
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return getInteractionShape(state, world, pos);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return getInteractionShape(state, world, pos);
    }


    @Override
    public @NotNull VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return baseVoxelShape != null ? baseVoxelShape : Shapes.block();
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (this instanceof ITE) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof InteractableBlockEntity be) {
                return be.onInteract(state, world, pos, player, hand, ray);
            }
        }
        return super.use(state, world, pos, player, hand, ray);
    }
}
