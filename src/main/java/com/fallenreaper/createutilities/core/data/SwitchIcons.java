package com.fallenreaper.createutilities.core.data;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.datafixers.util.Pair;

public class SwitchIcons {
    public static final SwitchIcon PUNCHCARD_SWITCHBUTTON = new SwitchIcon(Pair.of(GuiTextures.BUTTON_EMPTY, GuiTextures.BUTTON_HOVER_EMPTY), Pair.of(GuiTextures.BUTTON_FILLED, GuiTextures.BUTTON_HOVER));

    public static final SwitchIcon SWITCHBUTTON = new SwitchIcon(Pair.of(GuiTextures.DEFAULT_BUTTON_EMPTY, GuiTextures.DEFAULT_BUTTON_HOVER_EMPTY), Pair.of(GuiTextures.DEFAULT_BUTTON_FILLED, GuiTextures.DEFAULT_BUTTON_HOVER));
}
