package com.fallenreaper.createutilities.utils.data;

import java.awt.*;

@SuppressWarnings("all")
@FunctionalInterface
public interface IDraggable {
    void onDrag(int mouseX, int mouseY, Point coords, boolean rightClick);
}
