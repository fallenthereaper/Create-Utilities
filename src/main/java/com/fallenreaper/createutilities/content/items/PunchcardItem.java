package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.sliding_door.ModifiedSlidingDoor;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class PunchcardItem extends BaseItem   {
    public PunchcardItem(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public int getMaxClicks() {
        return super.getMaxClicks()/4;
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level worldIn = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        ItemStack itemStack = player.getItemInHand(player.getUsedItemHand());


        if (player == null)
            return InteractionResult.FAIL;
//handle tag stuff
        if (!itemStack.hasTag()) {
            if (worldIn.isClientSide)
                return InteractionResult.SUCCESS;

            CompoundTag stackTag = itemStack.getOrCreateTag();
            if (!stackTag.contains("Saved")) {
                if (worldIn.getBlockState(pos).getBlock() instanceof ModifiedSlidingDoor || worldIn.getBlockState(pos.below(1)).getBlock() instanceof ModifiedSlidingDoor || worldIn.getBlockState(pos.above(1)).getBlock() instanceof ModifiedSlidingDoor) {
                    if (worldIn.isClientSide()) {
                        worldIn.playSound(player, player.getX(), player.getY(), player.getZ(), AllSoundEvents.CONTROLLER_PUT.getMainEvent(), SoundSource.PLAYERS, 0.75f, 0.5f);
                        return InteractionResult.SUCCESS;
                    }

                    stackTag.put("DoorPosition", NbtUtils.writeBlockPos(pos));
                    LangBuilder lang = Lang.builder(CreateUtilities.ID);
                    player.displayClientMessage(lang.translate("door_bind.set").component().append(" ").append("At:" + " " + NbtUtils.readBlockPos(stackTag.getCompound("DoorPosition"))).withStyle(ChatFormatting.GREEN), true);
                    itemStack.setTag(stackTag);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (worldIn.getBlockState(pos).getBlock() instanceof ModifiedSlidingDoor || worldIn.getBlockState(pos.below(1)).getBlock() instanceof ModifiedSlidingDoor || worldIn.getBlockState(pos.above(1)).getBlock() instanceof ModifiedSlidingDoor) {
            if (itemStack.hasTag()) {
                if (itemStack.getTag().contains("DoorPosition")) {
                    BlockPos poss = NbtUtils.readBlockPos(itemStack.getTag().getCompound("DoorPosition"));
                    if (poss != pos.below(1) || poss != pos.above(1) || poss != pos) {
                        LangBuilder lang = Lang.builder(CreateUtilities.ID);
                        player.displayClientMessage(lang.translate("door_bind.invalid").component().append(":").append(" ").append("Non-Matching IDs").withStyle(ChatFormatting.RED), true);
                    }
                }
            }
    }

        CompoundTag tag = itemStack.getTag();
        CompoundTag newTag =  new CompoundTag();

        BlockPos selectedPos = NbtUtils.readBlockPos(tag.getCompound("DoorPosition"));

        if (player.isShiftKeyDown() && itemStack.hasTag()) {
            if (worldIn.isClientSide) {
                worldIn.playSound(player, player.getX(), player.getY(), player.getZ(), AllSoundEvents.CONTROLLER_TAKE.getMainEvent(), SoundSource.PLAYERS, 0.5f, 0.5f);
                return InteractionResult.SUCCESS;
            }
            LangBuilder lang = Lang.builder(CreateUtilities.ID);
            player.displayClientMessage(lang.translate("door_bind.clear").component().withStyle(ChatFormatting.YELLOW), true);
            itemStack.setTag(null);
            return InteractionResult.SUCCESS;
        }



        return super.useOn(pContext);

    }


}
