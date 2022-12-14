package com.fallenreaper.createutilities.core.data;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.datafixers.util.Pair;
@SuppressWarnings("ALL")
public class SwitchIcon {
    protected Pair<? extends GuiTextures, ? extends GuiTextures> offTexture;
    protected Pair<? extends GuiTextures, ? extends GuiTextures> onTexture;

    public SwitchIcon(Pair<? extends GuiTextures, ? extends GuiTextures> offTexture,Pair<? extends GuiTextures, ? extends GuiTextures> onTexture ) {
        this.offTexture = offTexture;
        this.onTexture = onTexture;
    }

    public GuiTextures getFull() {
        return onTexture.getFirst();
    }

    public GuiTextures getHovered() {
        return onTexture.getSecond();
    }

    public GuiTextures getEmpty() {
        return offTexture.getFirst();
    }

    public GuiTextures getHoveredEmpty() {
        return offTexture.getSecond();
    }

    public SwitchIcon switchTextures() {
        this.offTexture = onTexture;
        return this;
    }
}
