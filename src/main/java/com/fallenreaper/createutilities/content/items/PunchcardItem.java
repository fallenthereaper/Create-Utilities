package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoor;
import com.fallenreaper.createutilities.data.doorlock.DoorLockManagerStored;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PunchcardItem extends BaseItem {
    public PunchcardItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxClicks() {
        return super.getMaxClicks() / 4;
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level worldIn = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        DoorLockManagerStored manager = CreateUtilities.DOORLOCK_MANAGER;
        ItemStack itemStack = player.getItemInHand(player.getUsedItemHand());
        Block block = worldIn.getBlockState(pos).getBlock();


        if (player == null)
            return InteractionResult.FAIL;

        if (worldIn.getBlockState(pos).getBlock() instanceof LockSlidingDoor || worldIn.getBlockState(pos.below(1)).getBlock() instanceof LockSlidingDoor || worldIn.getBlockState(pos.above(1)).getBlock() instanceof LockSlidingDoor) {
            if (itemStack.hasTag()) {
                if (itemStack.getTag().contains("DoorPosition")) {
                    BlockPos poss = NbtUtils.readBlockPos(itemStack.getTag().getCompound("DoorPosition"));
                    if (poss != pos.below(1) || poss != pos.above(1) || poss != pos) {
                        LangBuilder lang = Lang.builder(CreateUtilities.ID);
                        player.displayClientMessage(lang.translate("door_bind.invalid").component().append(":").append(" ").append("Non-Matching IDs").withStyle(ChatFormatting.RED), true);
                    }
                }

                return InteractionResult.PASS;
            }
        }
        return InteractionResult.SUCCESS;
    }
        public static InstructionManager getSavedData (ItemStack pStack){
            if (!pStack.hasTag())
                return null;
            if (!pStack.getTag()
                    .contains("SavedData"))
                return null;

            return InstructionManager.fromTag(pStack.getTagElement("SavedData"));
        }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        MutableComponent caret = new TextComponent("> ").withStyle(ChatFormatting.GRAY);
        MutableComponent arrow = new TextComponent("-> ").withStyle(ChatFormatting.GOLD);

        ChatFormatting format = ChatFormatting.YELLOW;
        if(pStack.hasTag()) {

            tooltip.add(arrow.copy()
                    .append(new TextComponent(pStack.getTag().getString("Description") + " " + "Id:" + " " + pStack.getTag().getUUID("Key").toString().substring(0, 6)).withStyle(format)));
        }

/*
                InstructionManager manager = getSavedData(pStack);

                if (manager == null || manager.savedInfo.isEmpty())
                    return;


                for (int i = 0; i < manager.savedInfo.size(); i++) {
                    PunchcardInfo info = manager.savedInfo.get(i);

                    if(info instanceof PunchcardDoorInfo doorInfo)


    }

 */
    }
    private static BlockPos lastShownPos = null;
    private static AABB lastShownAABB = null;
    public static AABB getMultiblockBounds(LevelAccessor level, BlockPos pos) {
        VoxelShape shape = level.getBlockState(pos)
                .getShape(level, pos);
        if (shape.isEmpty())
            return new AABB(pos);
        return shape.bounds()
                .move(level.getBlockState(pos.below(1)).getBlock() instanceof LockSlidingDoor ? pos.below(1) : pos).expandTowards(0, 1, 0);
    }
    @OnlyIn(Dist.CLIENT)
    public static void clientTick() {
        Player player = Minecraft.getInstance().player;

        if (player == null)
            return;
        ItemStack heldItemMainhand = player.getMainHandItem();
        if (!(heldItemMainhand.getItem() instanceof PunchcardItem))
            return;
        if (!heldItemMainhand.hasTag())
            return;
        CompoundTag stackTag = heldItemMainhand.getOrCreateTag();
        if (!stackTag.contains("DoorPosition"))
            return;



       if(GogglesItem.isWearingGoggles(player) && player.isShiftKeyDown()) {


           BlockPos selectedPos = NbtUtils.readBlockPos(stackTag.getCompound("DoorPosition"));
            if(player.level.getBlockState(selectedPos).getBlock() instanceof LockSlidingDoor) {
                if (!selectedPos.equals(lastShownPos)) {
                    lastShownAABB = getBounds(selectedPos);
                    lastShownPos = selectedPos;
                }


                CreateClient.OUTLINER.showAABB("target" + heldItemMainhand.getTag().getUUID("Key").toString().substring(0, 10), lastShownAABB)
                        .colored(new Color(255, 255, 255))
                        .lineWidth(1 / 16f);
            }

       }
    }

    @OnlyIn(Dist.CLIENT)
    private static AABB getBounds(BlockPos pos) {
        Level world = Minecraft.getInstance().level;



            return getMultiblockBounds(world, pos);


    }


}
