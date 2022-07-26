package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterScreen;
import com.fallenreaper.createutilities.content.items.BaseItem;
import com.fallenreaper.createutilities.content.items.InstructionManager;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.CUContainerTypes;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class ClientEvents {
      public static List<Block> blockList = CreateUtilities.blockList;
    int count;
    public static List<Block> getBlockList() {
        return blockList;
    }


    @SubscribeEvent
    public static void registerMenuScreens(FMLClientSetupEvent event) {

        MenuScreens.register(CUContainerTypes.TYPEWRITER.get(), TypewriterScreen::new);
    }
    public static void onServerTick(TickEvent.ServerTickEvent event) {
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

            Player player = event.player;
            ItemStack stackIn = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(stackIn.getItem() instanceof BaseItem item) {
                Minecraft.getInstance().player.chat("player tick");
                CompoundTag tag = stackIn.getOrCreateTag();

                if(player.getY() % 20 == 0) {
                    count++;
                }

                InstructionManager manager = new InstructionManager();
                tag.putInt("SavedData", count);

            }

    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

    @SubscribeEvent
    public void onLivingEntityUseItem(LivingEntityUseItemEvent event) {
        boolean isPlayer = event.getEntity() instanceof Player;
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
    public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
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


    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void addEntityRendererLayers(EntityRenderersEvent.AddLayers event) {}
    }
}
