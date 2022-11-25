package com.fallenreaper.createutilities.content.blocks.sprinkler;

import net.minecraft.util.StringRepresentable;

public enum HydrationState implements StringRepresentable {
    HYDRATING("hydrating"),
    EMPTY("empty"),
    FILLED("filled");

    private final String name;

     HydrationState(String pName) {
        this.name = pName;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return null;
    }
}
