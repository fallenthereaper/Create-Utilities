package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.data.PunchcardInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

public class InstructionEntry {

    public PunchcardInfo instruction;
    public BlockPos pos;

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();

        tag.put("InstructionValue", this.instruction.getTagInfo());
        tag.put("DoorPosition", NbtUtils.writeBlockPos(pos));

        return tag;
    }

    public static InstructionEntry fromTag(CompoundTag tag) {
        InstructionEntry entry = new InstructionEntry();
        entry.instruction = InstructionManager.getAllEntries(tag.getCompound("InstructionValue"));

        return entry;
    }

}
