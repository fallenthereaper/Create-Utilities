package com.fallenreaper.createutilities.data;

import com.fallenreaper.createutilities.CreateUtilities;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = CreateUtilities.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void gatherData(GatherDataEvent event) {
        CreateUtilities.LOGGER.debug("Gathering data for CreateUtilities ...");
        DataGenerator generator = event.getGenerator();


        // Register ponders and generate ponder lang
        // Register processing recipes


        CreateUtilities.LOGGER.debug("Finished gathering data for CreateUtilities");
    }
}