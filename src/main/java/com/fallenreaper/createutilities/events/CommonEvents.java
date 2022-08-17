package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.punchcard_writer.PunchcardWriterScreen;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoor;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterScreen;
import com.fallenreaper.createutilities.content.items.InstructionEntry;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.data.DoorLock;
import com.fallenreaper.createutilities.data.PunchcardDoorInfo;
import com.fallenreaper.createutilities.data.doorlock.DoorLockManager;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.CUContainerTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CommonEvents {
    public static Set<Block> blockList = CreateUtilities.BLOCKLIST;

    @SubscribeEvent
    public static void registerMenuScreens(FMLClientSetupEvent event) {
        MenuScreens.register(CUContainerTypes.PUNCHCARD_WRITER.get(), PunchcardWriterScreen::new);
        MenuScreens.register(CUContainerTypes.TYPEWRITER.get(), TypewriterScreen::new);
    }

    public static void onClientTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;

    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

    @SubscribeEvent
    public static void onLoadWorld(WorldEvent.Load event) {
        LevelAccessor world = event.getWorld();
        CreateUtilities.DOORLOCK_MANAGER.levelLoaded(world);
    }

    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;

    }

    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;

        CreateUtilities.DOORLOCK_MANAGER.tick();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!isGameActive())
            return;
        if (event.phase != TickEvent.Phase.END)
            return;
        if (event.side != LogicalSide.SERVER)
            return;
        if (event.player.isSpectator())
            return;
        // if (!(event.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BaseItem))
        //   return;


        Level world = Minecraft.getInstance().level;


    }

    //TODO, move this to punchcard item class
    @SubscribeEvent
    public void onLivingEntityUseItem(PlayerInteractEvent.RightClickBlock event) {
        boolean isPlayer = event.getEntity() instanceof Player;
        BlockPos clickedPos = event.getPos();
        BlockEntity be = event.getWorld().getBlockEntity(clickedPos);
        ItemStack itemStack = event.getItemStack();
        UUID id;
        List<InstructionEntry> list;

        if (!(event.getEntity() instanceof Player player))
            return;
        if (!(itemStack.getItem() instanceof PunchcardItem item))
            return;
        DoorLockManager doorManager = CreateUtilities.DOORLOCK_MANAGER;
        if (!itemStack.hasTag()) {
            if (player.getLevel().getBlockState(clickedPos).getBlock() instanceof LockSlidingDoor) {
                for (DoorLock man : CreateUtilities.DOORLOCK_MANAGER.dataList) {
                    if (clickedPos.equals(man.getBlockPos().below(1)) || clickedPos.equals(man.getBlockPos().above(1)) || clickedPos.equals(man.getBlockPos())) {
                        LangBuilder lang = Lang.builder(CreateUtilities.ID);
                        player.displayClientMessage(lang.translate("door_bind.set_already").component().withStyle(ChatFormatting.YELLOW), true);
                        if (player.getLevel().isClientSide)
                            player.getLevel().playSound(player, clickedPos, SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 0.75f, 0.75f);
                        return;
                    }
                }
                list = new ArrayList<>();

                InstructionEntry doorInstruction = new InstructionEntry();
                doorInstruction.instruction = new PunchcardDoorInfo();
                list.add(doorInstruction);

                CompoundTag tag = itemStack.getOrCreateTag();

                id = tag.contains("Key") ? tag.getUUID("Key") : UUID.randomUUID();
                tag.putUUID("Key", id);

                doorManager.add(new DoorLock(clickedPos, id, player.getUUID()));

                ListTag listTag = NBTHelper.writeCompoundList(list, InstructionEntry::write);
                tag.put("EntryValues", listTag);

                list = NBTHelper.readCompoundList(tag.getList("EntryValues", Tag.TAG_COMPOUND), InstructionEntry::fromTag);


                if (!list.isEmpty())
                    for (InstructionEntry entry : list) {

                        // System.out.println(entry.instruction.getLabeledText());
                        tag.putString("Description", entry.instruction.getLabeledText());

                    }

                LangBuilder lang = Lang.builder(CreateUtilities.ID);

                player.getCooldowns()
                        .addCooldown(itemStack.getItem(), 20 / 4);


                player.displayClientMessage(lang.translate("door_bind.set").component().withStyle(ChatFormatting.GREEN).append(" ").append(CreateUtilities.DOORLOCK_MANAGER.dataStored.get(tag.getUUID("Key")).blockPos.toString()), true);
                if (player.getLevel().isClientSide)
                    player.getLevel().playSound(player, clickedPos, AllSoundEvents.CONFIRM.getMainEvent(), SoundSource.BLOCKS, 0.75f, 0.75f);
                event.setUseBlock(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void onLivingEntityHurt(LivingHurtEvent event) {
    }

    @SubscribeEvent
    public void onLivingEntityDeath(LivingDeathEvent event) {

        event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public void onLivingEntityHeal(LivingHealEvent event) {
    }

    @SubscribeEvent
    public void onAnimalTame(AnimalTameEvent event) {
        boolean isPlayer = event.getEntity() instanceof Player;
    }

    @SubscribeEvent
    public void onShieldBlock(ShieldBlockEvent event) {
        boolean isPlayer = event.getEntity() instanceof Player;
    }

    @SubscribeEvent
    public void onProjectileImpact(ProjectileImpactEvent event) {
        boolean isPlayer = event.getEntity() instanceof Player;
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event) {
        boolean isPlayer = event.getEntity() instanceof Player;
    }

    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack item = event.getItemStack();
        if (!(item.getItem() instanceof BlockItem blockItem))
            return;
        if (blockItem.getBlock() != CUBlocks.BELLOWS.get())
            return;
        if (!event.getFace().equals(Direction.UP))
            return;

        BlockState state = event.getWorld().getBlockState(event.getPos());

        for (Block blocks : blockList) {
            if (state.getBlock() instanceof AbstractFurnaceBlock || state.getBlock().equals(blocks))
                event.setUseBlock(Event.Result.DENY);
        }
    }
}
