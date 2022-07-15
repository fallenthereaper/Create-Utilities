package com.fallenreaper.createutilities.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {
@SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Text event) {
    Minecraft mc = Minecraft.getInstance();

    if (!mc.level.isClientSide())
        return;

    if(mc.player != null) {

        String fps = mc.fpsString;



        Player player = mc.player;
        event.getLeft().add(fps);
    }
}

}
