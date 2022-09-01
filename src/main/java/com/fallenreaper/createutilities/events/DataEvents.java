package com.fallenreaper.createutilities.events;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.index.CUPonder;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = CreateUtilities.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEvents {
    public static Map<String, String> doorTranslations = new HashMap<>();
    public static Map<String, String> translations = new HashMap<>();
    public static List<String> writerTranslations = new ArrayList<>();
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void gatherData(GatherDataEvent event) {

        //Instructions
        add("Instruction", "textcard_info", "Text");
        add("Instruction", "door_info", "Door Lock");
        add("Instruction", "train_ticket", "Train Ticket");
        add("Instruction", "instruction", "Instruction");
        add("Instruction", "change_speed", "Change Speed");
        add("Instruction", "redstone_signal", "Redstone Signal");

        //Door Lock
        add("DoorLock", "invalid", "Interaction Failed");
        add("DoorLock", "set_already", "Link Is Already Set");
        add("DoorLock", "clear", "Link Cleared");
        add("DoorLock", "fail", "Link Failed");
        add("DoorLock", "set", "Link Set");
        add("DoorLock", "non_matching",  "Non-Matching IDs");

        //Misc
        generateLang("gui.punchcardwriter.button", "save", "Save");
        generateLang("gui.punchcardwriter.button", "reset", "Reset");
        generateLang("gui.punchcardwriter.button", "remove", "Remove");
        generateLang("gui.punchcardwriter.scroll_input", "title", "Settings");
        generateLang("gui.punchcardwriter.button", "close", "Close");
        generateLang("punchcard_info", "clear", "Cleared Punchcard Data");
        generateLang("display_source", "time_duration","Time Duration");
        generateLang("gui.typewriter.status", "show","Start");
        generateLang("punchcard_writer.text_icon", "empty","▒");
        generateLang("punchcard_writer.text_icon", "full","█");
        generateLang("bellow.content", "inventory_empty","None");
        generateLang("bellow.content", "fuel","Fuel");
        generateLang("bellow.content", "status","Status");
        generateLang("bellow.content", "time","Time Elapsed");
        generateLang("bellow.content.status", "active","Running");
        generateLang("bellow.content.status", "paused","Paused");
        generateLang("bellow.content.status", "off","Off");
        generateLang("sprinkler.content", "range","Range");
        generateLang("sprinkler.content", "units","blocks");

        //Ponder
        generateLang("ponder.sprinkler", "description_intro","Hydrating Farmland areas using a sprinkler");
        generateLang("ponder.sprinkler", "description_1","Sprinkler can be used to hydrate farmland and speed up crops growth");


        CUPonder.register();
        PonderLocalization.provideRegistrateLang(CreateUtilities.registrate());

        for (Map.Entry<String, String> entry : translations.entrySet()) {
            generateLang("instruction", entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, String> entry : doorTranslations.entrySet()) {
            generateLang("door_bind", entry.getKey(), entry.getValue());
        }

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
