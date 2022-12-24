package com.fallenreaper.createutilities.core.client;

import com.fallenreaper.createutilities.CreateUtilities;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CUModelLayers {
    public static List<Pair<Supplier<ModelLayerLocation>, Supplier<LayerDefinition>>> MODELS_LAYERS = new ArrayList<>();

    public static ModelLayerLocation
            ENGINEER_GEAR_LAYER = createLocation("engineer_gear", "main");

    public static ModelLayerLocation
            BRASS_JETPACK_LAYER = createLocation("brass_jetpack", "main");



   static {
       addModelLayer(() -> ENGINEER_GEAR_LAYER, EngineerGearModel::createArmorLayer);
       addModelLayer(() -> BRASS_JETPACK_LAYER, BrassJetPackModel::createBodyLayer);
   }


    public static void registerAll(EntityRenderersEvent.RegisterLayerDefinitions event) {
       event.registerLayerDefinition(ENGINEER_GEAR_LAYER, EngineerGearModel::createArmorLayer);
       event.registerLayerDefinition(BRASS_JETPACK_LAYER, BrassJetPackModel::createBodyLayer);
       /*
        for (Pair<Supplier<ModelLayerLocation>, Supplier<LayerDefinition>> layer : MODELS_LAYERS) {
            event.registerLayerDefinition(layer.getFirst().get(), layer.getSecond());
        }

        */
    }

    private static ModelLayerLocation createLocation(String model, String layer) {
        return new ModelLayerLocation(new ResourceLocation(CreateUtilities.ID, model), layer);
    }

    public static void addModelLayer(Supplier<ModelLayerLocation> modelLoc, Supplier<LayerDefinition> layerDefinition) {
        MODELS_LAYERS.add(Pair.of(modelLoc, layerDefinition));
    }
}
