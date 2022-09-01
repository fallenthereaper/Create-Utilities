package com.fallenreaper.createutilities.utils.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("ALL")
public class PunchcardWriterManager {
   private static Map<UUID, PunchcardWriter> savedWriters = new HashMap<>();

    public static void replaceWriter(PunchcardWriter type, UUID key) {
        savedWriters.replace(key, type);
    }

    public static void addWriter(PunchcardWriter type, UUID key) {
        savedWriters.put(key, type);
    }

    public static void clearWriters() {
        savedWriters.clear();
    }

    public static void removeWriter(UUID key) {
        if(isEmpty())
            return;

        savedWriters.remove(key);
    }

    public static boolean isEmpty() {
        return savedWriters.isEmpty();
    }

    public static boolean hasWriter(UUID key) {
        return savedWriters.containsKey(key);
    }

    public static PunchcardWriter getWriter(UUID key) {
        return savedWriters.get(key);
    }

    protected void init() {
        savedWriters = new HashMap<>();
    }
}
