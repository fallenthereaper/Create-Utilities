package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.management.PunchcardInfo;
import com.fallenreaper.createutilities.content.management.PunchcardInstruction;
import com.simibubi.create.content.logistics.trains.management.schedule.Schedule;
import com.simibubi.create.content.logistics.trains.management.schedule.ScheduleEntry;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class InstructionManager {
    public static List<ResourceLocation> INSTRUCTIONS_LOCATIONS = new ArrayList<>();
    public static List<PunchcardInfo> INSTRUCTIONS = new ArrayList<>();

    protected static void addInstruction(String name, Supplier<PunchcardInfo> sup) {
            INSTRUCTIONS.add(sup.get());
            INSTRUCTIONS_LOCATIONS.add(CreateUtilities.defaultResourceLocation(name));

    }

   static {
        addInstruction("send_signal", PunchcardInstruction::new);
        addInstruction("train_ticket", PunchcardInstruction::new);
   }

    public List<ScheduleEntry> entries;
    public boolean cyclic;
    public int savedProgress;

    public InstructionManager() {
        entries = new ArrayList<>();
        cyclic = true;
        savedProgress = 0;
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        ListTag list = NBTHelper.writeCompoundList(entries, ScheduleEntry::write);
        tag.put("Entries", list);
        tag.putBoolean("Cyclic", cyclic);
        if (savedProgress > 0)
            tag.putInt("Progress", savedProgress);
        return tag;
    }

    public static Schedule fromTag(CompoundTag tag) {
        Schedule schedule = new Schedule();
        schedule.entries = NBTHelper.readCompoundList(tag.getList("Entries", Tag.TAG_COMPOUND), ScheduleEntry::fromTag);
        schedule.cyclic = tag.getBoolean("Cyclic");
        if (tag.contains("Progress"))
            schedule.savedProgress = tag.getInt("Progress");
        return schedule;
    }
}
