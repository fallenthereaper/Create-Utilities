package com.fallenreaper.createutilities;

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
