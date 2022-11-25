package com.fallenreaper.createutilities.utils.data;

import com.jozufozu.flywheel.core.source.parse.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class PunchcardWriterManager {

    private static Map<String, PunchcardWriter> SAVED_WRITERS = new HashMap<>();
    private static List<PunchcardWriter> TRACKED_WRITERS = new ArrayList<>();

    public static void replaceWriter(PunchcardWriter type, String key) {
        SAVED_WRITERS.replace(key, type);
    }

    public static void addWriter(PunchcardWriter type, String key) {
        SAVED_WRITERS.put(key, type);
        TRACKED_WRITERS.add(type);
        Variable  a;
    }

    public static void clearWriters() {
        SAVED_WRITERS.clear();
        TRACKED_WRITERS.removeAll(TRACKED_WRITERS);
    }

    public static void removeWriter(String key) {
        if(isEmpty())
            return;

        SAVED_WRITERS.remove(key);

    }

    public static boolean isEmpty() {
        return SAVED_WRITERS.isEmpty();
    }

    public static boolean hasWriter(String key) {
        return SAVED_WRITERS.containsKey(key);
    }

    public static PunchcardWriter getWriter(String key) {
        return SAVED_WRITERS.get(key);
    }

    protected void init() {
        SAVED_WRITERS = new HashMap<>();
    }
}
