package com.fallenreaper.createutilities.utils.data;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.datafixers.util.Pair;

public class SwitchIcon {
    protected Pair<? extends GuiTextures, ? extends GuiTextures> offTexture;
    protected Pair<? extends GuiTextures, ? extends GuiTextures> onTexture;

    public SwitchIcon(Pair<? extends GuiTextures, ? extends GuiTextures> offTexture,Pair<? extends GuiTextures, ? extends GuiTextures> onTexture ) {
        this.offTexture = offTexture;
        this.onTexture = onTexture;
    }

    public GuiTextures getFilled() {
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
}
