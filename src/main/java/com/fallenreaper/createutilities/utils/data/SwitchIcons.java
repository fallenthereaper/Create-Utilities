package com.fallenreaper.createutilities.utils.data;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.datafixers.util.Pair;

public class SwitchIcons {
    public static final SwitchIcon PUNCHCARD_SWITCHBUTTON = new SwitchIcon(Pair.of(GuiTextures.BUTTON_EMPTY, GuiTextures.BUTTON_HOVER_EMPTY), Pair.of(GuiTextures.BUTTON_FILLED, GuiTextures.BUTTON_HOVER));

    public static final SwitchIcon TYPEWRITER_SWITCHBUTTON = new SwitchIcon(Pair.of(GuiTextures.TYPEWRITER_BUTTON_EMPTY, GuiTextures.TYPEWRITER_BUTTON_HOVER_EMPTY), Pair.of(GuiTextures.TYPEWRITER_BUTTON_FILLED, GuiTextures.TYPEWRITER_BUTTON_HOVER));
}
