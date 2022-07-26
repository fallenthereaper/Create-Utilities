package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.data.PunchcardInfo;
import com.fallenreaper.createutilities.data.PunchcardInstruction;
import com.fallenreaper.createutilities.data.PunchcardTrainTicket;
import com.fallenreaper.createutilities.data.TextPunchcardInfo;
import com.simibubi.create.foundation.utility.LangBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class InstructionManager {

    public static List<LangBuilder> INSTRUCTIONS_LOCATIONS = new ArrayList<>();
    public static List<PunchcardInfo> INSTRUCTIONS = new ArrayList<>();
    public static LangBuilder lang = new LangBuilder(CreateUtilities.ID).text("instruction");

    public List< PunchcardInfo> savedInfo;
    public int savedProgress;

   static void addInstruction(String name, Supplier<PunchcardInfo> sup) {


        INSTRUCTIONS.add(sup.get());
         INSTRUCTIONS_LOCATIONS.add(lang.translate(name));
    }

   static {
       //TODO, figure out why is it stacking for every getLabeledText
        addInstruction("send_signal", () -> new PunchcardInstruction(lang.translate("send_signal").string()));
        addInstruction("train_ticket", () -> new PunchcardTrainTicket(lang.translate("train_ticket").string()));
        addInstruction("info_text", () -> new TextPunchcardInfo(lang.translate("text_info").string()));
       addInstruction("description", () -> new TextPunchcardInfo(lang.translate("description").string()));

   }

    public InstructionManager() {
        savedInfo = new ArrayList<>();
    }


}
