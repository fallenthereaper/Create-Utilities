package com.fallenreaper.createutilities.core.data.punchcard;

import com.fallenreaper.createutilities.CreateUtilities;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class InstructionManager {

    public static List<LangBuilder> INSTRUCTIONS_LOCATIONS = new ArrayList<>();
    public static List<Supplier<? extends PunchcardInfo>> INSTRUCTIONS = new ArrayList<>();
    public static LangBuilder lang = new LangBuilder(CreateUtilities.ID).text("instruction.");

    static {
        //TODO, figure out why is it stacking for every getLabeledText));
        addInstruction("door_info", PunchcardDoorInfo::new);
        addInstruction("text_info", TextPunchcardInfo::new);
        addInstruction("train_ticket_info", PunchcardTrainTicket::new);


    }

    public List<InstructionEntry> savedInfo;
    public CompoundTag doorPos;
    public int savedProgress;

    public InstructionManager() {
        savedInfo = new ArrayList<>();
    }

    static void addInstruction(String name, Supplier<? extends PunchcardInfo> sup) {


        INSTRUCTIONS.add(sup);
        INSTRUCTIONS_LOCATIONS.add(lang.translate(name));
    }

    public static InstructionManager fromTag(CompoundTag tag) {

        InstructionManager manager = new InstructionManager();

        manager.doorPos = tag.getCompound("Pos");
        manager.savedInfo = NBTHelper.readCompoundList(tag.getList("EntryValues", Tag.TAG_COMPOUND), InstructionEntry::fromTag);

        return manager;
    }

    public static PunchcardInfo getAllEntries(CompoundTag tag) {
        String message = tag.getString("Id");

        Supplier<? extends PunchcardInfo> supplier = null;

        for (Supplier<? extends PunchcardInfo> pair : InstructionManager.INSTRUCTIONS)
            supplier = pair;

        if (supplier == null) {
            CreateUtilities.LOGGER.warn("Punchcard Instruction of type: " + message + " " + "could not be found");
            return new PunchcardDoorInfo();
        }

        PunchcardInfo punchcards = supplier.get();

        punchcards.data = tag.getCompound("Data");


        return punchcards;
    }

    public CompoundTag write(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        ListTag list = NBTHelper.writeCompoundList(savedInfo, InstructionEntry::write);
        CompoundTag blockpos = NbtUtils.writeBlockPos(pos);
        tag.put("Pos", blockpos);
        tag.put("EntryValues", list);

        return tag;
    }
}
