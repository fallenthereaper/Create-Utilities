package com.fallenreaper.createutilities;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.fallenreaper.createutilities.index.CUPonder;
import com.simibubi.create.content.contraptions.goggles.GoggleOverlayRenderer;
import com.simibubi.create.content.curiosities.armor.CopperBacktankArmorLayer;
import com.simibubi.create.content.curiosities.toolbox.ToolboxHandlerClient;
import com.simibubi.create.content.curiosities.tools.BlueprintOverlayRenderer;
import com.simibubi.create.content.logistics.item.LinkedControllerClientHandler;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CreateUtilitiesClient {


    public static void onClientStartUp(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(CreateUtilitiesClient::clientInit);

    }

    public static void clientInit(final FMLClientSetupEvent event) {


        registerOverlays();
    }

    private static void registerOverlays() {
        // Register overlays in reverse order
  //      OverlayRegistry.registerOverlayAbove(ForgeIngameGui.AIR_LEVEL_ELEMENT, "Create's Remaining Air", CopperBacktankArmorLayer.REMAINING_AIR_OVERLAY);
        //     OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Create's Toolboxes", ToolboxHandlerClient.OVERLAY);
        //     OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Create's Goggle Information", GoggleOverlayRenderer.OVERLAY);
        //    OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Create's Blueprints", BlueprintOverlayRenderer.OVERLAY);
        //     OverlayRegistry.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "Create's Linked Controller", LinkedControllerClientHandler.OVERLAY);
    }

}
