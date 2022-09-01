package com.fallenreaper.createutilities.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;

import java.util.UUID;

public class NbtModUtils {
    public static DoorLock readDoorLock(CompoundTag tag) {
        UUID id = tag.getUUID("Id");
        UUID owner = tag.getUUID("OwnerId");
        BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("Position"));
        return new DoorLock(pos, id, owner);
    }

    public static CompoundTag writeDoorLock(DoorLock doorLock) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", doorLock.id);
        tag.putUUID("OwnerId", doorLock.ownerId);
        tag.put("Position", NbtUtils.writeBlockPos(doorLock.blockPos));
        return tag;
    }
}
