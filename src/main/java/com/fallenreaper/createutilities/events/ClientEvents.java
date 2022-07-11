package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterScreen;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.CUContainerTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.util.Supplier;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class ClientEvents {
      public static List<Block> blockList = new ArrayList<>();

    public static List<Block> getBlockList() {
        return blockList;
    }
    public static void addToBlockList(Supplier<Block> sup) {
        if(!(sup.get() == null))
       blockList.add(sup.get());
    }

    @SubscribeEvent
    public static void registerMenuScreens(FMLClientSetupEvent event) {

        MenuScreens.register(CUContainerTypes.TYPEWRITER.get(), TypewriterScreen::new);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) {} else if (event.side.isServer()) {}
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

        if (state.getBlock() instanceof AbstractFurnaceBlock)
            event.setUseBlock(Event.Result.DENY);
    }


    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void addEntityRendererLayers(EntityRenderersEvent.AddLayers event) {}
    }
}
