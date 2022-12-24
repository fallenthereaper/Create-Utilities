package com.fallenreaper.createutilities.core.events;

import com.fallenreaper.createutilities.core.client.CUModelLayers;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientHandler {

/*
    public static ModelLayerLocation
            ENGINEER_TOP_HAT_LAYER = new ModelLayerLocation(new ResourceLocation(CreateUtilities.ID, "engineer_top_hat"), "main");

 */
   // public static EngineerGear ENGINEER_TOP_HAT_MODEL = null;

    @SubscribeEvent
    public void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
      //  event.registerLayerDefinition(ENGINEER_TOP_HAT_LAYER, EngineerGear::createArmorLayer);
        CUModelLayers.registerAll(event);
    }

    @SubscribeEvent
    public void onRegisterLayers(EntityRenderersEvent.AddLayers event) {
     //   CUModelLayers.registerModelLayer(event);
    }

   public static IClientItemExtensions getArmorRenderer() {
        return new ArmorRenderProperties();
    }
}
