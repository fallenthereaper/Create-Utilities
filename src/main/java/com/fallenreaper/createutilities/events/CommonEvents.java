package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoor;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterScreen;
import com.fallenreaper.createutilities.content.items.InstructionEntry;
import com.fallenreaper.createutilities.content.items.InstructionManager;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.data.PunchcardDoorInfo;
import com.fallenreaper.createutilities.data.doorlock.DoorLockManager;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.CUContainerTypes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
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
import java.util.UUID;

@Mod.EventBusSubscriber
public class CommonEvents {
      public static List<Block> blockList = CreateUtilities.blockList;
    int count;
    public static List<Block> getBlockList() {
        return blockList;
    }


    @SubscribeEvent
    public static void registerMenuScreens(FMLClientSetupEvent event) {

        MenuScreens.register(CUContainerTypes.TYPEWRITER.get(), TypewriterScreen::new);
    }
    public  void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;
        Create.SCHEMATIC_RECEIVER.tick();
        Create.LAGGER.tick();
        ServerSpeedProvider.serverTick();
        Create.RAILWAYS.sync.serverTick();
    }
    public static void onClientTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            return;
        Create.SCHEMATIC_RECEIVER.tick();
        Create.LAGGER.tick();
        ServerSpeedProvider.serverTick();
        Create.RAILWAYS.sync.serverTick();
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

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }
    @SubscribeEvent
    public static void onLoadWorld(WorldEvent.Load event) {
        LevelAccessor world = event.getWorld();
        CreateUtilities.DOORLOCK_MANAGER.levelLoaded(world);
    }
    @SubscribeEvent
    public void onLivingEntityUseItem(PlayerInteractEvent.RightClickBlock event) {
        boolean isPlayer = event.getEntity() instanceof Player;
        BlockPos clickedPos = event.getPos();
        BlockEntity be = event.getWorld().getBlockEntity(clickedPos);
        ItemStack itemStack = event.getItemStack();
        UUID id;
        List<InstructionEntry> list;

        if(!(event.getEntity() instanceof Player player))
            return;
        if(!(itemStack.getItem() instanceof PunchcardItem item))
                return;

        InstructionManager manager;
        if(!itemStack.hasTag()) {
            if(player.getLevel().getBlockState(clickedPos).getBlock() instanceof LockSlidingDoor) {
                for (BlockPos positerator : CreateUtilities.DOORLOCK_MANAGER.currentPositions)
                    if (!positerator.equals(clickedPos.below(1)) || positerator.equals(clickedPos.above(1)) || positerator.equals(clickedPos)) {
                        LangBuilder lang = Lang.builder(CreateUtilities.ID);
                        player.displayClientMessage(lang.translate("door_bind.set_already").component().withStyle(ChatFormatting.YELLOW), true);
                    }


                list = new ArrayList<>();
                DoorLockManager doorManager = CreateUtilities.DOORLOCK_MANAGER;
                InstructionEntry doorInstruction = new InstructionEntry();
                doorInstruction.instruction = new PunchcardDoorInfo();

                list.add(doorInstruction);

                CompoundTag tag = itemStack.getOrCreateTag();

                CompoundTag blockTag = NbtUtils.writeBlockPos(clickedPos);

                CompoundTag blockTagf = NbtUtils.writeBlockState(event.getWorld().getBlockState(clickedPos));

                ListTag listTag = NBTHelper.writeCompoundList(list, InstructionEntry::write);

                tag.put("EntryValues", listTag);
                tag.put("DoorState", blockTagf);


                id = tag.contains("Key") ? tag.getUUID("Key") : UUID.randomUUID();

                tag.putUUID("Key", id);

                doorManager.addBlockPos(NbtUtils.readBlockPos(blockTag), tag.getUUID("Key"));

                tag.put("DoorPosition", NbtUtils.writeBlockPos(doorManager.doorLocksNetwork.get(id)));
              ;
                list = NBTHelper.readCompoundList(tag.getList("EntryValues", Tag.TAG_COMPOUND), InstructionEntry::fromTag);


              if(!list.isEmpty())
                for (int i = 0; i < list.size(); i++) {

                    InstructionEntry entry = list.get(i);


                        System.out.println(entry.instruction.getLabeledText());
                        tag.putString("Description", entry.instruction.getLabeledText());

                }

                LangBuilder lang = Lang.builder(CreateUtilities.ID);
                for(ItemStack itmem : player.getInventory().items)
                    if(itmem.getItem() instanceof PunchcardItem) {
                        if (itmem.hasTag() && itmem.getTag().contains("DoorPosition")) {
                            player.getCooldowns()
                                    .addCooldown(itmem.getItem(), 20 / 4);
                        }
                    }
                player.displayClientMessage(lang.translate("door_bind.set").component().withStyle(ChatFormatting.GREEN).append(" ").append( NbtUtils.readBlockPos(tag.getCompound("DoorPosition")).toString()), true);
                ;
                event.setUseBlock(Event.Result.ALLOW);
            }

        }
         if(itemStack.hasTag() && player.isShiftKeyDown() && !( player.getLevel().getBlockState(clickedPos).getBlock() instanceof LockSlidingDoor)) {
            if (player.getLevel().isClientSide) {
                player.getLevel().playSound(player, player.getX(), player.getY(), player.getZ(), AllSoundEvents.CONTROLLER_TAKE.getMainEvent(), SoundSource.PLAYERS, 0.5f, 0.5f);
            }
            LangBuilder lang = Lang.builder(CreateUtilities.ID);
            player.displayClientMessage(lang.translate("door_bind.clear").component().withStyle(ChatFormatting.YELLOW), true);
            CreateUtilities.DOORLOCK_MANAGER.removeBlockPos(itemStack.getTag().getUUID("Key"));
            itemStack.setTag(null);
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
    //Player Specific Events
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

        for(Block blocks : blockList) {
            if (state.getBlock() instanceof AbstractFurnaceBlock || state.getBlock().equals(blocks))
                event.setUseBlock(Event.Result.DENY);
        }
    }
}
