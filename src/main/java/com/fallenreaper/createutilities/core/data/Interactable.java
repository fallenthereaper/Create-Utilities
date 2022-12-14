package com.fallenreaper.createutilities.core.data;

import java.awt.*;

@SuppressWarnings("all")
public interface Interactable {

    @FunctionalInterface
    public interface IDraggable {
        void onDrag(int mouseX, int mouseY, Point coords, boolean rightClick);
    }

    @FunctionalInterface
    public interface IClickable {
        void onClick(int mouseX, int mouseY, Point coords, boolean rightClick);
    }
}