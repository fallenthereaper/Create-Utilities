package com.fallenreaper.createutilities.content.blocks.sliding_door;

import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.simibubi.create.content.curiosities.deco.SlidingDoorBlock;
import com.simibubi.create.content.curiosities.deco.SlidingDoorTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class LockSlidingDoor extends SlidingDoorBlock {
    public LockSlidingDoor(Properties p_52737_) {
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
        ItemStack item = pPlayer.getItemInHand(pHand);
        CompoundTag copy = item.getOrCreateTag();
        CompoundTag tag = new CompoundTag();
        if(item.getItem() instanceof PunchcardItem && item.hasTag() && item.getTag().contains("DoorPosition")) {
            BlockPos bos = NbtUtils.readBlockPos(item.getTag().getCompound("DoorPosition"));

          if(bos.equals(pPos.above(1)) || bos.equals(pPos.below(1)) || bos.equals(pPos) ) {
              CompoundTag ta = NbtUtils.writeBlockPos(bos);
              tag.put("SavedInfo", ta);
              BlockPos ppos = NbtUtils.readBlockPos(tag.getCompound("SavedInfo"));
              System.out.println(ppos);
              super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);

              return InteractionResult.SUCCESS;
          }


        }


        return InteractionResult.PASS;
    }


}
