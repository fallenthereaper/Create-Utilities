package com.fallenreaper.createutilities.utils.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class PunchcardTextWriter {

    public int count;
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

    public int getFillPercentage() {
        return Math.max(0, Math.min(this.count, getXsize() * getYsize()));
    }

    void add(int value) {
        this.count += Math.max(value, 0);;
    }

    void subtract(int value) {
        this.count -= Math.max(value, 0);
    }

    public String getRawText() {
        String base = "";
        for (String[] map : dataMap) {
            for (int col = 0; col < this.dataMap[1].length; col++) {
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
    void setBox(Point point) {
        dataMap[point.y % dataMap.length][point.x % dataMap[1].length] = this.empty;
    }

    /**
     * Fills a box at the specified position.
     */
    void fillBox(Point point) {
        dataMap[point.y % dataMap.length][point.x % dataMap[1].length] = this.full;
    }

    /**
     * Adds a box at the specified position.
     */
    private void addBox(Point point, String type) {
        if (dataMap[1].length <= 0 || dataMap[0].length <= 0) return;

        dataMap[point.y][point.x] = type;
    }

    /**
     * This must be called immediately after instancing otherwise it will cause a null pointer exception.
     *
     * @param x size
     * @param y size
     */
    public PunchcardTextWriter writeText(int x, int y) {
        int safeX = Math.min(16, x);
        int safeY = Math.min(16, y);
        dataMap = new String[safeY][safeX];

        for (int xx = 0; xx < safeY; xx++)
            for (int yy = 0; yy < safeX; yy++)
                this.addBox(new Point(yy, xx), this.full);

        return this;
    }


    /**
     * Instantly sets all boxes inside the square.
     */
    void set() {
        for (int xx = 0; xx < dataMap.length; xx++)
            for (int yy = 0; yy < dataMap[1].length; yy++)
                this.addBox(new Point(yy, xx), this.empty);
    }

    /**
     * Instantly fills all boxes inside the square.
     */
    void fill() {
        for (int xx = 0; xx < dataMap.length; xx++)
            for (int yy = 0; yy < dataMap[1].length; yy++)
                this.addBox(new Point(yy, xx), this.full);
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