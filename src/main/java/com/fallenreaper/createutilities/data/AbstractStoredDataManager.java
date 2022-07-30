package com.fallenreaper.createutilities.data;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;

import java.util.*;

public abstract class AbstractStoredDataManager<T> {
    public T type;
    public List<T> dataList;
    public Map<UUID, T> dataStored;

    public AbstractStoredDataManager() {
        clean();
    }

    private void clean(){
        dataStored = new HashMap<>();
        dataList = new LinkedList<>();
    }

    public void add(T type, UUID uuid) {
        dataStored.put(uuid, type);
        dataList.add(type);
    }

    public void remove(UUID id) {
        T removed = dataStored.remove(id);
        if (removed == null)
            return;

        dataList.remove(removed);
    }

    public void levelLoaded(LevelAccessor level) {
        MinecraftServer server = level.getServer();
        if (server == null || server.overworld() != level)
            return;
        clean();
    }

}
