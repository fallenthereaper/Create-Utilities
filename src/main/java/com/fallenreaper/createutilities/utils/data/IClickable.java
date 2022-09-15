package com.fallenreaper.createutilities.utils.data;

import java.awt.*;

@SuppressWarnings("all")
@FunctionalInterface
public interface IClickable {
    void onClick(int mouseX, int mouseY, Point coords, boolean rightClick);
}