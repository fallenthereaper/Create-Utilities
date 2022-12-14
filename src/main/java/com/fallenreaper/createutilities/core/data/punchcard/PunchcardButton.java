package com.fallenreaper.createutilities.core.data.punchcard;

import com.fallenreaper.createutilities.core.data.SwitchButton;
import com.fallenreaper.createutilities.core.data.SwitchIcon;
import com.fallenreaper.createutilities.core.data.SwitchIcons;

@SuppressWarnings("ALL")
public class PunchcardButton extends SwitchButton {

    protected PunchcardButton(int x, int y, int width, int height, SwitchIcon switchIcon) {
        super(x, y, width, height, switchIcon);

    }
    protected PunchcardButton(int x, int y, int width, int height) {
        this(x, y, width, height, SwitchIcons.PUNCHCARD_SWITCHBUTTON);
    }
}
