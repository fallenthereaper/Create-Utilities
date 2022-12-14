package com.fallenreaper.createutilities.core.data.doorlock;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.core.data.DoorLock;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DoorLockSavedData<T> extends SavedData {
    private Map<UUID, T> saved = new HashMap<>();

    private DoorLockSavedData() {
    }

    public static DoorLockSavedData load(CompoundTag nbt) {
        DoorLockSavedData doorLockManager = new DoorLockSavedData();
        doorLockManager.saved = new HashMap<>();
        NBTHelper.iterateCompoundList(nbt.getList("DoorsLocks", Tag.TAG_COMPOUND), c -> {
            DoorLock doorLock = DoorLock.read(c);
            doorLockManager.saved.put(doorLock.id, doorLock);
        });


        return doorLockManager;
    }

    public static DoorLockSavedData load(MinecraftServer server) {
        return server.overworld()
                .getDataStorage()
                .computeIfAbsent(DoorLockSavedData::load, DoorLockSavedData::new, "door_locks");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        DoorLockManager doors = CreateUtilities.DOORLOCK_MANAGER;

        nbt.put("DoorsLocks", NBTHelper.writeCompoundList(doors.dataStored.values(), DoorLock::write));

        return nbt;
    }

    public Map<UUID, T> getDoorLocks() {
        return saved;
    }

}
