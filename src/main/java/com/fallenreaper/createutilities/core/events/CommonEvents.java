package com.fallenreaper.createutilities.core.events;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowBlockEntity;
import com.fallenreaper.createutilities.content.blocks.punchcard_writer.PunchcardWriterScreen;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoor;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoorBlockEntity;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterScreen;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.core.data.DoorLock;
import com.fallenreaper.createutilities.core.data.doorlock.DoorLockManager;
import com.fallenreaper.createutilities.core.data.punchcard.InstructionEntry;
import com.fallenreaper.createutilities.core.data.punchcard.PunchcardDoorInfo;
import com.fallenreaper.createutilities.index.CUContainerTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CommonEvents {
    public static List<Block> BLOCK_LIST = CreateUtilities.BLOCKLIST;

    @SubscribeEvent
    public static void registerMenuScreens(FMLClientSetupEvent event) {
        MenuScreens.register(CUContainerTypes.PUNCHCARD_WRITER.get(), PunchcardWriterScreen::new);
        MenuScreens.register(CUContainerTypes.TYPEWRITER.get(), TypewriterScreen::new);
    }

    public static void onClientTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
        }

    }

    public static void itemExpire(ItemExpireEvent itemExpireEvent) {
        PunchcardItem.removeAfterDespawn(itemExpireEvent);

    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

    @SubscribeEvent
        public static void onLoadWorld(LevelEvent.Load event) {
        LevelAccessor world = event.getLevel();
        CreateUtilities.DOORLOCK_MANAGER.levelLoaded(world);
    }

    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
        }

        Level level = Minecraft.getInstance().level;


    }

    public void onWorldTick(TickEvent.LevelTickEvent event) {
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
         Player player = event.player;
        removeMiningFatigue(player);


    }

    public static void removeMiningFatigue(Player player) {
        if(!affects(player))
            return;
        if(player.getEffect(MobEffects.DIG_SLOWDOWN) == null)
            return;

        if(player.hasEffect(MobEffects.DIG_SLOWDOWN))
            player.removeEffect(MobEffects.DIG_SLOWDOWN);
    }


    public static boolean affects(LivingEntity entity) {
        if (!AllItems.DIVING_BOOTS.get()
                .isWornBy(entity)) {
            entity.getPersistentData()
                    .remove("HeavyBoots");
            return false;
        }

        NBTHelper.putMarker(entity.getPersistentData(), "HeavyBoots");
        if (!entity.isInWater())
            return false;
        if (entity.getPose() == Pose.SWIMMING)
            return false;
        if (entity instanceof Player) {
            Player playerEntity = (Player) entity;
            if (playerEntity.getAbilities().flying)
                return false;
        }
        return true;
    }



    //todo, redo this so it doesn't use block pos but uuid, there's a better way to do it by checking rope spool in aeronautics
    //TODO, move this to punchcard item class
    @SubscribeEvent
    public void onLivingEntityUseItem(PlayerInteractEvent.RightClickBlock event) {
        boolean isPlayer = event.getEntity() != null;
        BlockPos clickedPos = event.getPos();
        BlockEntity be = event.getLevel().getBlockEntity(clickedPos);
        ItemStack itemStack = event.getItemStack();
        UUID id;
        List<InstructionEntry> list;

        Player player = event.getEntity();
        if (!(itemStack.getItem() instanceof PunchcardItem item))
            return;
        if (!(player.getLevel().getBlockState(clickedPos).getBlock() instanceof LockSlidingDoor))
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

                registerKey(tag, player, clickedPos, list);

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


    public void registerKey(CompoundTag tag, Player player, BlockPos clickedPos, @Nullable List<InstructionEntry> list) {
        UUID id;
        id = tag.contains("Key") ? tag.getUUID("Key") : UUID.randomUUID();
        tag.putUUID("Key", id);
        if (player.getLevel().getBlockEntity(clickedPos) instanceof LockSlidingDoorBlockEntity te) {
            DoorLock doorLock = new DoorLock(clickedPos, id, player.getUUID());
            CreateUtilities.DOORLOCK_MANAGER.add(doorLock);
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

    @SubscribeEvent
    public void onLivingEntityHurt(LivingHurtEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts

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

    //todo move this to BellowBlock
    @SubscribeEvent
    public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        BellowBlockEntity.cancelRightClickInteraction(event);

    }

    @SubscribeEvent
    public void rightClick(PlayerInteractEvent.RightClickBlock event) {
        PunchcardItem.rightClick(event);
    }
}
