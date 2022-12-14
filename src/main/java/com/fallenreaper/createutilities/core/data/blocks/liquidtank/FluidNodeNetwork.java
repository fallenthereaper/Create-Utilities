package com.fallenreaper.createutilities.core.data.blocks.liquidtank;

import java.util.*;

public class FluidNodeNetwork {
    private static Map<UUID, FluidNode> SAVED_NODES = new HashMap<>();
    private static List<FluidNode> TRACKED_NODES = new ArrayList<>();

    public static void replaceNode(FluidNode type, UUID key) {
        SAVED_NODES.replace(key, type);
    }

    public static void addNode(FluidNode type, UUID key) {
        SAVED_NODES.put(key, type);
        TRACKED_NODES.add(type);

    }

    public static void clearNodes() {
        SAVED_NODES.clear();

    }

    public static void removeNode(UUID key) {
        if(isEmpty())
            return;

        SAVED_NODES.remove(key);

    }

    public static boolean isEmpty() {
        return SAVED_NODES.isEmpty();
    }

    public static boolean hasNode(UUID key) {
        return SAVED_NODES.containsKey(key);
    }

    public static FluidNode getNode(UUID key) {
        return SAVED_NODES.get(key);
    }

    protected void init() {
        SAVED_NODES = new HashMap<>();
    }
}
