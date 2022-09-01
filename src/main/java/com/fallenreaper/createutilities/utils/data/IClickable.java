package com.fallenreaper.createutilities.utils.data;

import java.awt.*;
@SuppressWarnings("all")
public interface IClickable {
    void onDrag(int mouseX, int mouseY, Point coords, boolean rightClick, int buttonId);

    void onClick(int mouseX, int mouseY, Point coords, boolean rightClick, int buttonId);

    void onRelease(int mouseX, int mouseY, Point coords, boolean rightClick, int buttonId);
}
