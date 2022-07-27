package com.fallenreaper.createutilities.content.blocks.sliding_door;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.simibubi.create.content.curiosities.deco.SlidingDoorBlock;
import com.simibubi.create.content.curiosities.deco.SlidingDoorTileEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
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
        ItemStack item = pPlayer.getItemInHand(pHand);
        CompoundTag copy = item.getOrCreateTag();
        if(item.getItem() instanceof PunchcardItem) {
            if(item.hasTag()) {

                CompoundTag tag = item.getTag();
                copy.put("Saved", NbtUtils.writeBlockPos(NbtUtils.readBlockPos(tag.getCompound("DoorPosition"))));
                System.out.println(copy.getCompound("Saved"));
              if(tag.contains("DoorPosition") && NbtUtils.readBlockPos(tag.getCompound("DoorPosition")).equals(pPos) || NbtUtils.readBlockPos(tag.getCompound("DoorPosition")).equals(pPos.below(1))|| NbtUtils.readBlockPos(tag.getCompound("DoorPosition")).equals(pPos.above(1))) {


                  super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
                  return InteractionResult.SUCCESS;
              }
            }
        }
        if(item.getItem() instanceof PunchcardItem) {
            if(item.hasTag()) {

                if (copy.contains("Saved")) {

                   BlockPos p = NbtUtils.readBlockPos(copy.getCompound("Saved"));

                    System.out.println(p + "test");
                    if(pPos.equals(p)) {
                        LangBuilder lang = Lang.builder(CreateUtilities.ID);
                        pPlayer.displayClientMessage(lang.translate("door_bind.set_already").component().withStyle(ChatFormatting.YELLOW), true);

                        return InteractionResult.PASS;
                    }
                }
            }
        }


        return InteractionResult.PASS;
    }

}
