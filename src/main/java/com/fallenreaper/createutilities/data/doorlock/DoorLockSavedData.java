package com.fallenreaper.createutilities.data.doorlock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class DoorLockSavedData<T> extends SavedData {
    private Map<UUID, T> saved = new HashMap<>();

    @Override
    public abstract CompoundTag save(CompoundTag nbt);

    abstract DoorLockManagerStored load(CompoundTag nbt);


    public abstract Map<UUID, BlockPos> getBlockPos();


    private DoorLockSavedData() {}

    public abstract DoorLockSavedData load(MinecraftServer server);

}
