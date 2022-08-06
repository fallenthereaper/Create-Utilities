package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoor;
import com.fallenreaper.createutilities.content.items.data.BoxFrame;
import com.fallenreaper.createutilities.content.items.data.PunchcardTextWriter;
import com.fallenreaper.createutilities.data.doorlock.DoorLockManager;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        if(itemStack.hasTag() && pPlayer.isShiftKeyDown()) {
            if (pPlayer.getLevel().isClientSide) {
                pPlayer.getLevel().playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), AllSoundEvents.CONTROLLER_TAKE.getMainEvent(), SoundSource.PLAYERS, 0.5f, 0.5f);
            }
            LangBuilder lang = Lang.builder(CreateUtilities.ID);
            pPlayer.displayClientMessage(lang.translate("door_bind.clear").component().withStyle(ChatFormatting.YELLOW), true);
            CreateUtilities.DOORLOCK_MANAGER.remove(itemStack.getTag().getUUID("Key"));
            itemStack.setTag(null);
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level worldIn = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        DoorLockManager manager = CreateUtilities.DOORLOCK_MANAGER;
        ItemStack itemStack = player.getItemInHand(player.getUsedItemHand());
        Block block = worldIn.getBlockState(pos).getBlock();


        if (player == null)
            return InteractionResult.FAIL;

        if (worldIn.getBlockState(pos).getBlock() instanceof LockSlidingDoor || worldIn.getBlockState(pos.below(1)).getBlock() instanceof LockSlidingDoor || worldIn.getBlockState(pos.above(1)).getBlock() instanceof LockSlidingDoor) {
            if (itemStack.hasTag()) {
                if (itemStack.getTag().contains("Key")) {
                    BlockPos poss = manager.dataStored.get(itemStack.getTag().getUUID("Key")).blockPos;
                    if (poss != pos.below(1) || poss != pos.above(1) || poss != pos) {
                        LangBuilder lang = Lang.builder(CreateUtilities.ID);
                        player.displayClientMessage(lang.translate("door_bind.invalid").component().append(":").append(" ").append("Non-Matching IDs").withStyle(ChatFormatting.RED), true);
                        if(player.getLevel().isClientSide)
                            player.getLevel().playSound(player, pos, AllSoundEvents.DENY.getMainEvent(), SoundSource.BLOCKS, 0.75f, 0.75f );
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

        ChatFormatting format = ChatFormatting.GOLD;
        if (pStack.hasTag() && pStack.getTag().contains("Key")) {
            PunchcardTextWriter writer = new PunchcardTextWriter();
            writer.writeText(5 , 7);

            for(int i = 0; i < 2; i++) {

                    writer.setBox(new BoxFrame(i, 0));

            }


            tooltip.add(arrow.withStyle(format).copy()
                    .append(new TextComponent(pStack.getTag().getString("Description")).withStyle(format)));

            for (int i = 1; i < writer.getYsize() + 1; i++) {
                int max = i * writer.getXsize();
                int min = Math.max(max - writer.getXsize(), 0);
                tooltip.add(new TextComponent("    " + writer.drawBox().substring(min, max)).withStyle(ChatFormatting.YELLOW));
            }


          //   tooltip.add(new TextComponent("     " + writer.drawBox().substring(3, 7)).withStyle(ChatFormatting.YELLOW));
          //  tooltip.add(new TextComponent("     " + writer.drawBox().substring(6, 10)).withStyle(ChatFormatting.YELLOW));
          //  tooltip.add(new TextComponent("     " + writer.drawBox().substring(9, 13)).withStyle(ChatFormatting.YELLOW));
           // tooltip.add(new TextComponent("     " + writer.drawBox().substring(12, 16)).withStyle(ChatFormatting.YELLOW));
        }
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
        if (!stackTag.contains("Key"))
            return;



       if(GogglesItem.isWearingGoggles(player) && player.isShiftKeyDown()) {

           assert heldItemMainhand.getTag() != null;
           BlockPos selectedPos = CreateUtilities.DOORLOCK_MANAGER.dataStored.get(stackTag.getUUID("Key")).blockPos;
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
