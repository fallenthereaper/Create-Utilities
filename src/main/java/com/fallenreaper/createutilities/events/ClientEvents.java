package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoor;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        Minecraft mc = Minecraft.getInstance();

        if (!mc.level.isClientSide())
            return;

        if (mc.player != null) {

            String fps = mc.fpsString;


            Player player = mc.player;
            // event.getLeft().add(fps);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (!isGameActive())
            return;

        Level world = Minecraft.getInstance().level;
        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        Player player = Minecraft.getInstance().player;

        if (player == null)
            return;
        if (world == null)
            return;


        for (ItemStack item : player.getInventory().items)
            if (item.getItem() instanceof PunchcardItem) {
                if (item.hasTag() && item.getTag().contains("Key")) {
                    CompoundTag stackTag = item.getTag();

                    BlockPos selectedBlockPos = CreateUtilities.DOORLOCK_MANAGER.dataStored.get(stackTag.getUUID("Key")).blockPos;

                    if (!(player.level.getBlockState(selectedBlockPos).getBlock() instanceof LockSlidingDoor))
                        return;
                    //   item.setTag(null);

                }
            }
        PunchcardItem.clientTick();


    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }

}
