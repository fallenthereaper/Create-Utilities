package com.fallenreaper.createutilities.data;

import com.fallenreaper.createutilities.content.items.InstructionEntry;
import com.fallenreaper.createutilities.content.items.InstructionManager;
import net.minecraft.nbt.CompoundTag;

public class PunchcardNetwork {


    public static InstructionEntry instructionEntryFromTag(CompoundTag tag) {
        InstructionEntry entry = new InstructionEntry();
        entry.instruction = InstructionManager.getAllEntries(tag.getCompound("Instruction"));

        return entry;
    }
}
