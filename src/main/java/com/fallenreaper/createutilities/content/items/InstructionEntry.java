package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.data.PunchcardInfo;
import net.minecraft.nbt.CompoundTag;

public class InstructionEntry {

    public PunchcardInfo instruction;

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();

        tag.put("InstructionValue", this.instruction.getTagInfo());

        return tag;
    }

    public static InstructionEntry fromTag(CompoundTag tag) {
        InstructionEntry entry = new InstructionEntry();
        entry.instruction = InstructionManager.getAllEntries(tag.getCompound("InstructionValue"));

        return entry;
    }

}
