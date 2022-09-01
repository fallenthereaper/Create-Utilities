package com.fallenreaper.createutilities.utils.data;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.awt.*;

@SuppressWarnings("ALL")
public class PunchcardButton extends SwitchButton {
    private PunchcardWriter writer;

    protected PunchcardButton(int x, int y, int width, int height, SwitchIcon switchIcon, PunchcardWriter writer) {
        super(x, y, width, height, switchIcon);
        this.writer = writer;
    }

    protected PunchcardButton(int x, int y, int width, int height, PunchcardWriter writer) {
        this(x, y, width, height, SwitchIcons.PUNCHCARD_SWITCHBUTTON, writer);
    }

    @Override
    protected void onLeftDrag(Point point, double pMouseX, double pMouseY, int pButton) {
        this.playDownSound(Minecraft.getInstance().getSoundManager(), true);
        this.writer.onDrag((int) pMouseX, (int) pMouseY, point, false, pButton);
    }

    @Override
    protected void onRightDrag(Point point, double pMouseX, double pMouseY, int pButton) {
        this.playDownSound(Minecraft.getInstance().getSoundManager(), false);
        this.writer.onDrag((int) pMouseX, (int) pMouseY, point, true, pButton);
    }

    @Override
    protected void onLeftClick(double pMouseX, double pMouseY, int pButton) {
        this.playDownSound(Minecraft.getInstance().getSoundManager(), true);
        this.writer.onClick((int) pMouseX, (int) pMouseY, new Point(this.x, this.y), false, pButton);
    }

    @Override
    protected void onRightClick(double pMouseX, double pMouseY, int pButton) {
        this.playDownSound(Minecraft.getInstance().getSoundManager(), false);
        this.writer.onClick((int) pMouseX, (int) pMouseY, new Point(this.x, this.y), true, pButton);
    }

    @Override
    protected boolean isValidLeftClickButton(int pButton) {
        //0
        return pButton == 0;
    }

    @Override
    protected boolean isValidRightClickButton(int pButton) {
        //1
        return pButton == 1;
    }

    @Override
    protected SoundEvent getLeftClickSound() {
        return SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT;
    }

    @Override
    protected SoundEvent getRightClickSound() {
        return SoundEvents.BOOK_PAGE_TURN;
    }

    public PunchcardWriter getWriter() {
        return this.writer;
    }

}
