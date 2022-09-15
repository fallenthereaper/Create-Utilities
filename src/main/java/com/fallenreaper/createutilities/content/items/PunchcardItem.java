package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.punchcard_writer.PunchcardWriterBlockEntity;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoor;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoorBlockEntity;
import com.fallenreaper.createutilities.data.DoorLock;
import com.fallenreaper.createutilities.data.doorlock.DoorLockManager;
import com.fallenreaper.createutilities.networking.ModPackets;
import com.fallenreaper.createutilities.networking.PunchcardWriterEditPacket;
import com.fallenreaper.createutilities.utils.IHaveHiddenToolTip;
import com.fallenreaper.createutilities.utils.data.PunchcardWriter;
import com.fallenreaper.createutilities.utils.data.PunchcardWriterManager;
import com.jozufozu.flywheel.repack.joml.Math;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PunchcardItem extends BaseItem implements IHaveHiddenToolTip {
    private static BlockPos lastShownPos = null;
    private static AABB lastShownAABB = null;

    public PunchcardItem(Properties pProperties) {
        super(pProperties);
    }

    public static InstructionManager getSavedData(ItemStack pStack) {
        if (!pStack.hasTag())
            return null;
        if (!pStack.getTag()
                .contains("SavedData"))
            return null;

        return InstructionManager.fromTag(pStack.getTagElement("SavedData"));
    }

    //todo add holdShift function
    public static void addToolTip(List<Component> toolTips, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof PunchcardItem))
            return;
        if (!itemStack.hasTag())
            return;

        if (itemStack.hasTag()) {
            if (itemStack.getTag().contains("WriterKey")) {
                if (PunchcardWriterManager.hasWriter(itemStack.getTag().getUUID("WriterKey"))) {
                    UUID key = itemStack.getTag().getUUID("WriterKey");
                    PunchcardWriter writer = PunchcardWriterManager.getWriter(key);
                   // toolTips.add(writer.getTextWriter().getYsize() + 2, writer.getProgressBar());
                }
            }
        }
    }

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

        if (GogglesItem.isWearingGoggles(player) && player.isShiftKeyDown()) {
            assert heldItemMainhand.getTag() != null;
            BlockPos selectedPos = CreateUtilities.DOORLOCK_MANAGER.dataStored.get(stackTag.getUUID("Key")).blockPos;
            if (player.level.getBlockState(selectedPos).getBlock() instanceof LockSlidingDoor) {
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

    public static boolean validateKey(Level world, BlockPos pos) {
        if (world == null)
            return false;
        if (!world.isLoaded(pos))
            return false;
        return world.getBlockEntity(pos) instanceof LockSlidingDoorBlockEntity;
    }

    public static void rightClick(PlayerInteractEvent.RightClickBlock event) {
        ItemStack itemStack = event.getItemStack();
        BlockPos pos = event.getPos();

        if (!(event.getEntity() instanceof Player player))
            return;
        if (!(itemStack.getItem() instanceof PunchcardItem))
            return;
        if (!(player.getLevel().getBlockEntity(pos) instanceof PunchcardWriterBlockEntity be))
            return;


        //  BlockEntity te = be;
        //   BlockState block = player.getLevel().getBlockState(pos);

        if (be.inventory.getStackInSlot(0).isEmpty()) {
            if (itemStack.isEmpty()) {
                event.setUseBlock(Event.Result.DENY);
            }
            be.inventory.insertItem(0, itemStack, false);
            ModPackets.channel.sendToServer(new PunchcardWriterEditPacket(be.inventory, itemStack));
            player.getInventory().removeItem(itemStack);

            event.setUseBlock(Event.Result.ALLOW);
        }


    }

    public static void removeAfterDespawn(ItemExpireEvent itemExpireEvent) {
        ItemStack itemStack = itemExpireEvent.getEntityItem().getItem();

        if (!(itemStack.getItem() instanceof PunchcardItem))
            return;

        if (itemStack.hasTag()) {
            if (itemStack.getTag().contains("WriterKey")) {
                if (PunchcardWriterManager.hasWriter(itemStack.getTag().getUUID("WriterKey"))) {
                    PunchcardWriterManager.removeWriter(itemStack.getTag().getUUID("WriterKey"));
                    itemExpireEvent.setResult(Event.Result.ALLOW);
                }
            }
        }
    }

    @Override
    public void onDestroyed(@NotNull ItemEntity pItemEntity) {
        super.onDestroyed(pItemEntity);
        ItemStack itemStack = pItemEntity.getItem();
        if (itemStack.hasTag()) {
            if (itemStack.getTag().contains("WriterKey")) {
                if (PunchcardWriterManager.hasWriter(itemStack.getTag().getUUID("WriterKey"))) {
                    PunchcardWriterManager.removeWriter(itemStack.getTag().getUUID("WriterKey"));
                }
            }
        }
    }

    public void registerKey(CompoundTag tag, Player player, BlockPos clickedPos, @Nullable List<InstructionEntry> list) {
        UUID id;
        id = tag.contains("Key") ? tag.getUUID("Key") : UUID.randomUUID();
        tag.putUUID("Key", id);
        if (player.getLevel().getBlockEntity(clickedPos) instanceof LockSlidingDoorBlockEntity te) {
            DoorLock doorLock = new DoorLock(clickedPos, id, player.getUUID());

            te.createLock(doorLock);

            assert list != null;
            ListTag listTag = NBTHelper.writeCompoundList(list, InstructionEntry::write);
            tag.put("EntryValues", listTag);
            list = NBTHelper.readCompoundList(tag.getList("EntryValues", Tag.TAG_COMPOUND), InstructionEntry::fromTag);


            if (!list.isEmpty())
                for (InstructionEntry entry : list) {

                    // System.out.println(entry.instruction.getLabeledText());
                    tag.putString("Description", entry.instruction.getLabeledText());

                }
        }
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        if (pStack.hasTag() && pStack.getItem() instanceof PunchcardItem) {
            if (pStack.getTag().contains("WriterKey")) {
                if (PunchcardWriterManager.hasWriter(pStack.getTag().getUUID("WriterKey"))) {
                    return false;
                    //PunchcardWriterManager.getWriter(pStack.getTag().getUUID("WriterKey")).getTextWriter().getCount() > 0;
                }
            }
        }
        return false;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        if (pStack.hasTag() && pStack.getItem() instanceof PunchcardItem) {
            if (pStack.getTag().contains("WriterKey")) {
                if (PunchcardWriterManager.hasWriter(pStack.getTag().getUUID("WriterKey"))) {
                    PunchcardWriter writer = PunchcardWriterManager.getWriter(pStack.getTag().getUUID("WriterKey"));
                    return Math.round(Math.min((13.0F * writer.getProgress()), 13.0F));
                }
            }
        }
        return 0;
    }

    public void onSlotChanged() {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        player.getLevel().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 0.55f, 1f);

    }

    @Override
    public int getBarColor(ItemStack pStack) {

        return Color.WHITE.getRGB();
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);


        if (itemStack.hasTag() && pPlayer.isShiftKeyDown()) {
            if (pPlayer.getLevel().isClientSide)
                pPlayer.getLevel().playSound(pPlayer, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), AllSoundEvents.CONTROLLER_TAKE.getMainEvent(), SoundSource.PLAYERS, 0.5f, 0.5f);

            LangBuilder lang = Lang.builder(CreateUtilities.ID);

            if (itemStack.getTag().contains("WriterKey")) {
                PunchcardWriterManager.removeWriter(itemStack.getTag().getUUID("WriterKey"));
                pPlayer.displayClientMessage(lang.translate("punchcard_info.clear").component().withStyle(ChatFormatting.YELLOW), true);
                itemStack.setTag(null);
            } else if (itemStack.getTag().contains("Key")) {
                CreateUtilities.DOORLOCK_MANAGER.remove(itemStack.getTag().getUUID("Key"));
                pPlayer.displayClientMessage(lang.translate("door_bind.clear").component().withStyle(ChatFormatting.YELLOW), true);
                itemStack.setTag(null);
            }


            return InteractionResultHolder.success(itemStack);
        }

        return InteractionResultHolder.success(itemStack);
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level worldIn = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        DoorLockManager manager = CreateUtilities.DOORLOCK_MANAGER;
        ItemStack itemStack = player.getItemInHand(player.getUsedItemHand());
        Block block = worldIn.getBlockState(pos).getBlock();


        if (worldIn.getBlockState(pos).getBlock() instanceof LockSlidingDoor || worldIn.getBlockState(pos.below(1)).getBlock() instanceof LockSlidingDoor || worldIn.getBlockState(pos.above(1)).getBlock() instanceof LockSlidingDoor) {
            if (itemStack.hasTag()) {
                if (itemStack.getTag().contains("Key")) {
                    BlockPos poss = manager.dataStored.get(itemStack.getTag().getUUID("Key")).blockPos;
                    if (poss != pos.below(1) || poss != pos.above(1) || poss != pos) {
                        LangBuilder lang = Lang.builder(CreateUtilities.ID);
                        player.displayClientMessage(lang.translate("door_bind.invalid").component().append(":").append(" ").append(lang.translate("door_bind.non_matching").string()).withStyle(ChatFormatting.RED), true);
                        if (player.getLevel().isClientSide)
                            player.getLevel().playSound(player, pos, AllSoundEvents.DENY.getMainEvent(), SoundSource.BLOCKS, 0.75f, 0.75f);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }
/**
 if (worldIn.getBlockState(pos).getBlock() instanceof PunchcardWriterBlock) {
 if (worldIn.getBlockEntity(pos) instanceof PunchcardWriterBlockEntity te && itemStack.getItem() instanceof PunchcardItem) {
 if (te.inventory != null) {
 if (te.inventory.getStackInSlot(0).isEmpty()) {
 te.inventory.setStackInSlot(0, itemStack);
 player.getInventory().removeItem(itemStack);

 //todo, remove player slot and maybe move this to an event and delete the inventory from PunchcardWriter
 return InteractionResult.SUCCESS;
 }
 }
 }
 }
 **/
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        MutableComponent caret = new TextComponent("> ").withStyle(ChatFormatting.GRAY);
        MutableComponent arrow = new TextComponent("-> ").withStyle(ChatFormatting.GOLD);

        ChatFormatting format = ChatFormatting.GOLD;
        if (pStack.hasTag() && pStack.getTag().contains("WriterKey") && pStack.getTag().contains("InstructionType") && PunchcardWriterManager.hasWriter(pStack.getTag().getUUID("WriterKey"))) {

            PunchcardWriter writer = PunchcardWriterManager.getWriter(pStack.getTag().getUUID("WriterKey"));
            tooltip.add(arrow.append("").append(new TextComponent(pStack.getTag().getString("InstructionType"))).withStyle(format));

            //  tooltip.add(arrow.withStyle(format).copy()
            //    .append(new TextComponent(pStack.getTag().getString("Description")).withStyle(format)));
            writer.toolTipFormat(tooltip, ChatFormatting.YELLOW, true, 0);
            //   tooltip.add(new TextComponent("     " + writer.drawBox().substring(3, 7)).withStyle(ChatFormatting.YELLOW));
            //  tooltip.add(new TextComponent("     " + writer.drawBox().substring(6, 10)).withStyle(ChatFormatting.YELLOW));
            //  tooltip.add(new TextComponent("     " + writer.drawBox().substring(9, 13)).withStyle(ChatFormatting.YELLOW));
            // tooltip.add(new TextComponent("     " + writer.drawBox().substring(12, 16)).withStyle(ChatFormatting.YELLOW));
        }
    }


    @Override
    public TextComponent getKey() {
        return new TextComponent(this.getDescriptionId());
    }

    @Override
    public <T extends LivingEntity> boolean poseArm(ItemStack itemStack, HumanoidArm arm, HumanoidModel<T> model, T entity, boolean rightHand) {
        if (itemStack.getItem() instanceof PunchcardItem && itemStack.hasTag()) {
            if (itemStack.getTag().contains("WriterKey")) {
                if (rightHand) {
                    //ADD static methods to automatically change arm animations

                    return true;
                } else {


                    return true;
                }

            }

        }
        return super.poseArm(itemStack, arm, model, entity, rightHand);
    }
}
