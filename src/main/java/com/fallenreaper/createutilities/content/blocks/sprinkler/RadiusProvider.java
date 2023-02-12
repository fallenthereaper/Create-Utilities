package com.fallenreaper.createutilities.content.blocks.sprinkler;


public interface RadiusProvider<T extends Integer> {
    T getRadius();

    void reset();
}
