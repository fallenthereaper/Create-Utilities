package com.fallenreaper.createutilities.data.doorlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;

import java.util.*;

public class DoorLockManager {

    public Map<UUID, BlockPos> doorLocksNetwork;
    public List<BlockPos> currentPositions;
    public DoorLockManager savedData;

  public DoorLockManager() {
        clean();
    }

    public void clean(){
      doorLocksNetwork = new HashMap<>();
      currentPositions = new LinkedList<>();
    }

    public void addBlockPos(BlockPos pos, UUID uuid) {
        doorLocksNetwork.put(uuid, pos);
        currentPositions.add(pos);
    }

    public void removeBlockPos(UUID id) {
        BlockPos removed = doorLocksNetwork.remove(id);
        if (removed == null)
            return;
        currentPositions.remove(removed);
    }

    public void levelLoaded(LevelAccessor level) {
        MinecraftServer server = level.getServer();
        if (server == null || server.overworld() != level)
            return;
        clean();
    }


}
