package com.fallenreaper.createutilities.content.blocks.steam_furnace;

public enum FurnaceState {
    PRODUCING,
    LIT,
    NONE, LOADED;


    public boolean isLit() {
        return this == LIT;
    }

    public boolean isProducing() {
        return this == PRODUCING;
    }

    public boolean isPaused() {
        return this == NONE;
    }

    public boolean isLoaded() {
        return this == LOADED;
    }
}
