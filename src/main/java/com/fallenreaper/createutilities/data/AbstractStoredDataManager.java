package com.fallenreaper.createutilities.data;

import com.fallenreaper.createutilities.data.doorlock.DoorLockSavedData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;

import java.util.*;

public abstract class AbstractStoredDataManager<T extends DoorLock> {
    public T type;
    public List<T> dataList;
    public Map<UUID, T> dataStored;
    public DoorLockSavedData<T> savedData;

    public AbstractStoredDataManager() {
        clean();
    }

    protected void clean() {
        dataStored = new HashMap<>();
        dataList = new LinkedList<>();
    }

    public void add(T type) {
        dataStored.put(type.id, type);
        dataList.add(type);
    }

    public void levelLoaded(LevelAccessor level) {
        MinecraftServer server = level.getServer();
        if (server == null || server.overworld() != level)
            return;
        savedData = null;
        loadData(server);
        clean();
    }


    private void loadData(MinecraftServer server) {
        if (savedData != null)
            return;

        savedData = DoorLockSavedData.load(server);
        dataStored = savedData.getDoorLocks();
        dataStored.values()
                .forEach(dataList::add);
    }

    public void remove(UUID id) {
        T removed = dataStored.get(id);
        if (removed == null)
            return;

        dataStored.remove(id);
        dataList.remove(removed);
    }

    public abstract void tick();


}
