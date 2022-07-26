package com.fallenreaper.createutilities.content.blocks.sliding_door;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.curiosities.deco.SlidingDoorBlock;
import com.simibubi.create.content.curiosities.deco.SlidingDoorTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ModifiedSlidingDoor extends SlidingDoorBlock {
    public ModifiedSlidingDoor(Properties p_52737_) {
        super(p_52737_);
    }

    public Class<SlidingDoorTileEntity> getTileEntityClass() {
        return SlidingDoorTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends SlidingDoorTileEntity> getTileEntityType() {
        return CUBlockEntities.SLIDING_DOOR.get();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pPlayer.getItemInHand(pHand).is(CUItems.PUNCHCARD.get())) {
            if(AllItems.WRENCH.isIn(pPlayer.getMainHandItem()))
                return InteractionResult.SUCCESS;

            if(pLevel.isClientSide)
                pLevel.playSound(pPlayer, pPos, SoundEvents.NETHER_BRICKS_STEP, SoundSource.BLOCKS, 0.25f, 0.15f);
            pPlayer.displayClientMessage(new TextComponent("Invalid Key:" + " " + pPlayer.getItemInHand(pHand).getItem().getName(pPlayer.getItemInHand(pHand)).getString()).withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.UNDERLINE), true);
            return InteractionResult.FAIL;
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
