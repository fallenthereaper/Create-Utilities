package com.fallenreaper.createutilities.utils.data;

@SuppressWarnings("ALL")
public class PunchcardButton extends SwitchButton {

    protected PunchcardButton(int x, int y, int width, int height, SwitchIcon switchIcon) {
        super(x, y, width, height, switchIcon);

    }
    protected PunchcardButton(int x, int y, int width, int height) {
        this(x, y, width, height, SwitchIcons.PUNCHCARD_SWITCHBUTTON);
    }
}
