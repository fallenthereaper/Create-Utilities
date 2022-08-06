package com.fallenreaper.createutilities.content.items.data;

import java.util.Map;
import java.util.TreeMap;

public class WriterArchived {
    String spacing = "    ";

    public char emptyBox = '\u2592';
    public char filledBox = '\u2588';

    public Map<Integer, Character> xCoordinates;
    public Map<Integer, Character> yCoordinates;

    public WriterArchived() {
        xCoordinates = new TreeMap<>();
        yCoordinates = new TreeMap<>();
    }

    private void addBox(BoxFrame boxFrame) {
        xCoordinates.put(boxFrame.x, filledBox);
        yCoordinates.put(boxFrame.y, filledBox);
    }

    private void removeBox(BoxFrame box){
        xCoordinates.remove(box.x);
        yCoordinates.remove(box.y);
    }

    public void setBox(BoxFrame box){
        xCoordinates.replace(box.x, emptyBox);
        yCoordinates.replace(box.y, emptyBox);
    }
    public String drawBox() {
        String base = " ";

        for (int i = 0; i < xCoordinates.size(); i++) {

            base += xCoordinates.get(i);
        }
        return base;

    }

    /**
     * This must be called immediately after instancing otherwise it will cause a null pointer exception
     * @param x size
     * @param y size
     */
    public void writeBox(int x, int y) {
        for (int xx = 0; xx < x  ; xx++) {

            this.addBox(new BoxFrame(xx, 0));

        }
    }
}