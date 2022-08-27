package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.CreateUtilities;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = CreateUtilities.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEvents {
   public static Map<String, String> doorTranslations = new HashMap<>();
   public static Map<String, String> translations = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void gatherData(GatherDataEvent event) {



        //Instructions
        add("Instruction","textcard_info", "Text");
        add("Instruction","door_info", "Door Lock");
        add("Instruction","train_ticket", "Train Ticket");
        add("Instruction","instruction", "Instruction");
        add("Instruction","change_speed", "Change Speed");

        //Door Lock
        add("DoorLock","invalid", "Interaction Failed");
        add("DoorLock","set_already", "Link Is Already Set");
        add("DoorLock","clear", "Link Cleared");
        add("DoorLock","fail", "Link Failed");
        add("DoorLock","set", "Link Set");

        for (Map.Entry<String, String> entry : translations.entrySet()) {
            generateLang("instruction", entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, String> entry : doorTranslations.entrySet()) {
            generateLang("door_bind", entry.getKey(), entry.getValue());
        }


        CreateUtilities.registrate().addRawLang("createutilities.sprinkler.ponder.description_1", "Hydrating Farmland areas using a sprinkler");
      ;
        CreateUtilities.registrate().addRawLang("createutilities.display_source.time_duration", "Time Duration");

    }

    public static void add(String type, String name, String translation) {
        switch (type) {
            case "DoorLock" -> doorTranslations.put(name, translation);
            case "Instruction" -> translations.put(name, translation);
        }
    }

    public static void generateLang(String type, String loc, String translation) {
        CreateUtilities.registrate().addRawLang(CreateUtilities.ID + "." + type + "." + loc, translation);
    }

}
