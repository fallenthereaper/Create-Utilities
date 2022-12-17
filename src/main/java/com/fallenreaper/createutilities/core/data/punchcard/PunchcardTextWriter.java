package com.fallenreaper.createutilities.core.data.punchcard;

import com.fallenreaper.createutilities.core.data.TextIcon;
import net.minecraft.nbt.CompoundTag;

import java.awt.*;

@SuppressWarnings("all")
public class PunchcardTextWriter {

    private int count;
    private String empty, full;
    private String[][] pixels;
    private byte color;
    private int width;
    private int height;

    public PunchcardTextWriter(TextIcon icon, int width, int height) {
        this.empty = icon.getEmptyIcon();
        this.full = icon.getFullIcon();
        this.width = width;
        this.height = height;
    }

    /**
     * Changes the icon type.
     */
    public PunchcardTextWriter setIcon(TextIcon icon) {
        this.empty = icon.getEmptyIcon();
        this.full = icon.getFullIcon();
        return this;
    }
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Width", this.getXsize());
        compound.putInt("Height", this.getYsize());
        compound.putInt("ProgressCounter", this.count);
        compound.putString("EmptyIcon",empty );
        compound.putString("FullIcon",full);
    }
    public void read(CompoundTag compound, boolean clientPacket) {
        this.count = compound.getInt("ProgressCounter");
        this.full = compound.getString("FullIcon");
        this.empty = compound.getString("EmptyIcon");
        this.width = compound.getInt("Width");
        this.height = compound.getInt("Height");
    }


    void add(int value) {
        this.count += Math.max(value, 0);
    }

    void subtract(int value) {
        this.count -= Math.max(value, 0);
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte newColor) {
        this.color = newColor;
    }

    public String getRawText() {
        var base = "";
        for (String[] map : pixels) {
            for (int col = 0; col < this.pixels[1].length; col++) {
                base += map[col];
            }
        }
        return base;
    }

    /**
     * Gets the size on the X axis.
     */
    public byte getXsize() {
        return (byte) pixels[1].length;
    }

    /**
     * Gets the size on the Y axis.
     */
    public byte getYsize() {
        return (byte) this.pixels.length;
    }

    public int getCount() {
        return Math.max(0, Math.min(this.count, getXsize() * getYsize()));
    }

    /**
     * Sets a box at the specified position.
     */
    void setPixel(Point point) {
        pixels[point.y % pixels.length][point.x % pixels[1].length] = this.empty;
        this.add(1);
    }

    /**
     * Fills a box at the specified position.
     */
    void fillPixel(Point point) {
        pixels[point.y % pixels.length][point.x % pixels[1].length] = this.full;
        this.subtract(1);
    }

    /**
     * Adds a box at the specified position.
     */
    private void addPixel(Point point, String type) {
        if (pixels[1].length <= 0 || pixels[0].length <= 0) return;

        pixels[point.y][point.x] = type;
    }

    /**
     * This must be called immediately after instancing otherwise it will cause a null pointer exception.
     *
     * @param x size
     * @param y size
     */
    public PunchcardTextWriter writeText() {
        int safeX = Math.min(16, width);
        int safeY = Math.min(16, height);
        this.pixels = new String[safeY][safeX];

        for (int xx = 0; xx < safeY; xx++)
            for (int yy = 0; yy < safeX; yy++)
                this.addPixel(new Point(yy, xx), this.full);

        return this;
    }

    public PunchcardTextWriter setWidth(int width) {
        this.width = width;
        writeText();
        return this;
    }
    public PunchcardTextWriter setHeight(int height) {
        this.height = height;
        writeText();
        return this;
    }

    /**
     * Instantly sets all boxes inside the square.
     */
    void set() {
        for (int xx = 0; xx < pixels.length; xx++)
            for (int yy = 0; yy < pixels[1].length; yy++)
                this.addPixel(new Point(yy, xx), this.empty);
    }

    /**
     * Instantly fills all boxes inside the square.
     */
    void fill() {
        for (int xx = 0; xx < pixels.length; xx++)
            for (int yy = 0; yy < pixels[1].length; yy++)
                this.addPixel(new Point(yy, xx), this.full);
    }



    @Deprecated
    public boolean find(Point pixel) {
        int[] value = new int[2];
        for (int yy = 0; yy < pixels[0].length; yy++)
            for (int xx = 0; xx < pixels[1].length; xx++)
                if (pixels[pixel.y][pixel.x] == empty) {

                    return true;
                }
        return false;
    }
}