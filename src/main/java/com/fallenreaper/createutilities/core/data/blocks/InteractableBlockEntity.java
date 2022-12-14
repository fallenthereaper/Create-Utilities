package com.fallenreaper.createutilities.core.data.blocks;

import com.fallenreaper.createutilities.core.utils.InteractionHandler;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public abstract class InteractableBlockEntity extends SmartTileEntity {
    protected List<InteractionHandler> interactionHandlers;

    public InteractableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        interactionHandlers = new ArrayList<>();
        ArrayList<InteractionHandler> list = new ArrayList<>();
        addInteractionHandler(list);
        interactionHandlers.addAll(list);

    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void tick() {
        super.tick();
        interactionHandlers.forEach(InteractionHandler::tick);
    }

    @Override
    protected void write(CompoundTag compoundTag, boolean clientPacket) {
        super.write(compoundTag, clientPacket);
        interactionHandlers.forEach(handler -> handler.write(compoundTag, clientPacket));
    }

    @Override
    protected void read(CompoundTag compoundTag, boolean clientPacket) {
        super.read(compoundTag, clientPacket);
        interactionHandlers.forEach(handler -> handler.read(compoundTag, clientPacket));
    }

    @Override
    public void writeSafe(CompoundTag tag, boolean clientPacket) {
        super.writeSafe(tag, clientPacket);
    }

    public abstract void addInteractionHandler(List<InteractionHandler> interactions);


    public InteractionResult onInteract(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        interactionHandlers.forEach(handler -> handler.onInteract(pState,  pLevel,  pPos,  pPlayer, pHand,  pHitResult));
        return InteractionResult.FAIL;
    }

    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getLightEmission();
    }

    public void onFall(Level pLevel,BlockPos pPos,  BlockState pState, Entity pEntity, float fallDistance) {
    }

    public int getOutputSignal() {
        return 0;
    }
}
