package com.fallenreaper.createutilities.utils.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public final class PunchcardTextWriter {

    private String empty;
    private String full;
    private String[][] dataMap;

    public PunchcardTextWriter(TextIcon icon) {
        this.empty = icon.getEmptyIcon();
        this.full = icon.getFullIcon();
    }

    /**
     * Changes the icon type.
     */
    public PunchcardTextWriter setIcon(TextIcon icon) {
        this.empty = icon.getEmptyIcon();
        this.full = icon.getFullIcon();
        return this;
    }

    /**
     * Draws a box with the specified dimensions.
     * This must be called after every change so that the changes can actually have effect.
     */
    public String getRawText() {
        String base = "";
        for (String[] map : dataMap) {
            for (int col = 0; col < dataMap[1].length; col++) {
                base += map[col];
            }
        }
        return base;
    }

    /**
     * Gets the size on the X axis.
     */
    public int getXsize() {
        return dataMap[1].length;
    }

    /**
     * Gets the size on the Y axis.
     */
    public int getYsize() {
        return dataMap.length;
    }

    /**
     * Sets a box at the specified position.
     */
    public void setBox(Point point) {
        dataMap[Math.min(dataMap.length - 1, point.y)][Math.min(dataMap[1].length - 1, point.x)] = empty;
    }

    /**
     * Fills a box at the specified position.
     */
    public void fillBox(Point point) {
        dataMap[Math.min(dataMap.length - 1, point.y)][Math.min(dataMap[1].length - 1, point.x)] = full;

    }

    /**
     * Adds a box at the specified position.
     */
    private void addBox(Point point, String type) {
        if (dataMap[1].length <= 0 || dataMap[0].length <= 0)
            return;

        dataMap[point.y][point.x] = type;
    }

    /**
     * This must be called immediately after instancing otherwise it will cause a null pointer exception.
     *
     * @param x size
     * @param y size
     */
    public PunchcardTextWriter writeText(int x, int y) {
        int safeX = Math.min(20, x);
        int safeY = Math.min(20, y);
        dataMap = new String[safeY][safeX];

        for (int xx = 0; xx < safeY; xx++)
            for (int yy = 0; yy < safeX; yy++)
                this.addBox(new Point(yy, xx), full);

        return this;
    }

    /**
     * Instantly sets all boxes inside the square.
     */
    public void setAll() {
        for (int xx = 0; xx < dataMap.length; xx++)
            for (int yy = 0; yy < dataMap[1].length; yy++)
                this.addBox(new Point(yy, xx), empty);
    }

    /**
     * Instantly fills all boxes inside the square.
     */
    public void fillAll() {
        for (int xx = 0; xx < dataMap.length; xx++)
            for (int yy = 0; yy < dataMap[1].length; yy++)
                this.addBox(new Point(yy, xx), full);
    }

    /**
     * Gets the lines between the string for parsing.
     */
    public TextComponent getLines(PunchcardTextWriter punchcardWriter, ChatFormatting formatting) {
        List<TextComponent> dataList = new ArrayList<>();

        for (int i = 1; i < punchcardWriter.getYsize() + 1; i++) {
            int max = i * punchcardWriter.getXsize();
            int min = Math.max(max - this.getXsize(), 0);
            dataList.add((TextComponent) new TextComponent("   " + punchcardWriter.getRawText().substring(min, max)).withStyle(formatting));
        }

        for (TextComponent component : dataList) {
            return component;
        }
        return null;
    }

    public boolean getAt(int x, int y) {
        int[] value = new int[2];
        for (int yy = 0; yy < dataMap[0].length; yy++)
            for (int xx = 0; xx < dataMap[1].length; xx++)
                if (dataMap[y][x] == empty) {

                    return true;
                }
        return false;
    }
}