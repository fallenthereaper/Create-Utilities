package com.fallenreaper.createutilities.core.data.punchcard;

import net.minecraft.nbt.CompoundTag;

public class PunchcardNetwork {


    public static InstructionEntry instructionEntryFromTag(CompoundTag tag) {
        InstructionEntry entry = new InstructionEntry();
        entry.instruction = InstructionManager.getAllEntries(tag.getCompound("Instruction"));

        return entry;
    }
}
