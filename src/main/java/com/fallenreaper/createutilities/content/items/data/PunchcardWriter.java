package com.fallenreaper.createutilities.content.items.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class PunchcardWriter {

    protected char emptyBox = '\u2592';
    protected char filledBox = '\u2588';
    private char[][] dataMap;

    /**
     * Draws a box with the specified dimensions.
     * This must be called after every change so that the changes can actually have effect.
     */
    public String drawBox() {
        String base = "";
        for (char[] map : dataMap) {
            for (int col = 0; col < dataMap[1].length; col++) {
                base += map[col];
            }
        }
        return base;
    }

    /**
     * Gets the size on the X axis.
     */
    public int getXsize (){
        return dataMap[1].length;
    }

    /**
     * Gets the size on the Y axis.
     */
    public int getYsize (){
        return dataMap.length;
    }

    /**
     * Sets a box at the specified position.
     */
    public void setBox(BoxFrame box) {
        dataMap[Math.min(dataMap.length - 1, box.y)][Math.min(dataMap[1].length - 1, box.x)] = emptyBox;

    }
    /**
     * Fills a box at the specified position.
     */
    public void fillBox(BoxFrame box) {
        dataMap[Math.min(dataMap.length - 1, box.y)][Math.min(dataMap[1].length - 1, box.x)] = filledBox;

    }

    private void addBox(BoxFrame boxFrame, char type) {
        if (dataMap[1].length <= 0 || dataMap[0].length <= 0)
            return;

        dataMap[boxFrame.y][boxFrame.x] = type;
    }

    /**
     * This must be called immediately after instancing otherwise it will cause a null pointer exception.
     * @param x size
     * @param y size
     */
    public void writeBox(int x, int y) {
        //For safety reasons I'm going to add a max cap to them.
        int safeX = Math.min(20, x);
        int safeY = Math.min(20, y);
        dataMap = new char[safeY][safeX];

        for (int xx = 0; xx < safeY; xx++)
            for (int yy = 0; yy < safeX; yy++)
                this.addBox(new BoxFrame(yy, xx), filledBox);
    }

    /**
     * Instantly sets all boxes inside the square.
     */
    public void setAll() {
        for (int xx = 0; xx < dataMap.length ; xx++)
            for (int yy = 0; yy < dataMap[1].length ; yy++)
                this.addBox(new BoxFrame(yy, xx), emptyBox);
    }

    /**
     * Instantly fills all boxes inside the square.
     */
    public void fillall() {
        for (int xx = 0; xx < dataMap.length ; xx++)
            for (int yy = 0; yy < dataMap[1].length ; yy++)
                this.addBox(new BoxFrame(yy, xx), filledBox);
    }

    /**
     * Gets the lines between the string for parsing.
     */
    public TextComponent getLines(PunchcardWriter punchcardWriter, ChatFormatting formatting){
        List<TextComponent> dataList = new ArrayList<>();

        for (int i = 1; i < punchcardWriter.getYsize() + 1; i++) {
            int max = i * punchcardWriter.getXsize();
            int min = Math.max(max - this.getXsize(), 0);
            dataList.add((TextComponent) new TextComponent("   " + punchcardWriter.drawBox().substring(min, max)).withStyle(formatting));
        }

        for (TextComponent component : dataList) {
            return component;
        }
        return null;
    }

    public int[] findBoxAt(int x, int y) {
        int[] value = new int[2];
        for (int yy = 0; yy < dataMap[0].length; yy++)
            for (int xx = 0; xx < dataMap[1].length; xx++)
                if(dataMap[x][y] == emptyBox) {
                    value[0] = x;
                    value[1] = y;
                }
        return value;
    }
}