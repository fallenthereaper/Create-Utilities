package com.fallenreaper.createutilities.utils.data;

import com.fallenreaper.createutilities.content.blocks.punchcard_writer.AbstractSmartContainerScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public final class PunchcardWriter {
    public PunchcardTextWriter textWriter;
    public PunchcardButton[][] coordinatesMap;
    public Map<Point, PunchcardButton> allButtons;
    Map<Integer, Integer> yCoords;
    Map<Integer, Integer> xCoords;
    private PunchcardButton button;
    private AbstractSmartContainerScreen<?> screen;
    private int xPosition;
    private int yPosition;
    private String defaultEmpty;
    private String defaultFull;
    private int width;
    private int height;

    private PunchcardWriter(AbstractSmartContainerScreen<?> screen, int x, int y, int width, int height) {
        this.defaultEmpty = "\u2592";
        this.defaultFull = "\u2588";
        this.height = height;
        this.width = width;
        this.textWriter = new PunchcardTextWriter(TextIcon.create(defaultFull, defaultEmpty)).writeText(width, height);
        this.screen = screen;
        this.xPosition = x;
        this.yPosition = y;
    }

    /**
     * Make sure to call {@code write()} right after.
     */
    public static PunchcardWriter create(AbstractSmartContainerScreen<?> screen, int x, int y, int width, int height) {
        return new PunchcardWriter(screen, x, y, width, height);
    }

    /**
     * Returns a copy of the specified {@link PunchcardWriter}
     */
    public static PunchcardWriter copy(PunchcardWriter copied$writer) {
        PunchcardWriter copy = new PunchcardWriter(copied$writer.screen, copied$writer.xPosition, copied$writer.yPosition, copied$writer.width, copied$writer.height);
        copy.textWriter = copied$writer.getTextWriter();
        copy.width = copied$writer.width;
        copy.height = copied$writer.height;
        copy.coordinatesMap = copied$writer.coordinatesMap;
        copy.defaultEmpty = copied$writer.defaultEmpty;
        copy.defaultFull = copied$writer.defaultFull;
        copy.xCoords = copied$writer.xCoords;
        copy.yCoords = copied$writer.yCoords;
        copy.xPosition = copied$writer.xPosition;
        copy.yPosition = copied$writer.yPosition;
        return copy;

    }

    /**
     * Adds a box and a button at the specified position.
     */
    private void addButton(Point position, PunchcardButton button) {
        if (coordinatesMap[1].length <= 0 || coordinatesMap[0].length <= 0)
            return;
        this.coordinatesMap[position.y][position.x] = button;
        this.allButtons.put(position, button);
    }

    /**
     * Sets a box and a button at the specified position.
     */
    void setBox(Point position) {
        this.textWriter.setBox(position);
    }

    /**
     * Fill a box and a button at the specified position.
     */
    void fillBox(Point position) {
        this.textWriter.fillBox(position);
    }

    /**
     * Changes the text icon.
     */
    public PunchcardWriter setIcon(TextIcon icon) {
        this.defaultFull = icon.getFullIcon();
        this.defaultEmpty = icon.getEmptyIcon();
        this.textWriter.setIcon(icon);
        return this;
    }

    /**
     * Handles the mapping of all buttons by attaching it's position to an index.
     */
    private void syncPositions() {
        this.yCoords = new HashMap<>(textWriter.getYsize());
        this.xCoords = new HashMap<>(textWriter.getXsize());
        for (int y = 1; y < textWriter.getYsize() + 1; y++) {
            for (int x = 1; x < textWriter.getXsize() + 1; x++) {
                this.button = coordinatesMap[y - 1][x - 1];
                this.yCoords.put(button.y, y);
                this.xCoords.put(button.x, x);
            }
        }
    }

    /**
     * Returns the instance of {@link PunchcardTextWriter}.
     */
    public PunchcardTextWriter getTextWriter() {
        return textWriter;
    }

    /**
     * Instantly disables and sets all boxes.
     */
    public PunchcardWriter setAll() {
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.state = PunchcardButton.Mode.OFF;
                this.textWriter.setAll();
            }
        }
        return this;
    }

    /**
     * Instantly fills all boxes.
     */
    public PunchcardWriter fillAll() {
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.state = PunchcardButton.Mode.ON;
                this.textWriter.fillAll();
            }
        }
        return this;
    }

    /**
     * Renders a square with the specified dimensions.
     */
    public PunchcardWriter draw(Font font, float x, float y, float scaleFactor, int rgb) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.scale(scaleFactor, scaleFactor, 0);
        poseStack.translate(x, y, 0);

        if (getTextWriter() != null)
            for (int i = 1; i < this.textWriter.getYsize() + 1; i++) {
                int max = i * this.textWriter.getXsize();
                int min = Math.max(max - textWriter.getXsize(), 0);
                font.drawShadow(poseStack, this.textWriter.getRawText().substring(min, max), x, ((9f * i) + y), rgb);
            }
        return this;
    }

    /**
     * Disables interaction with all buttons.
     */
    public PunchcardWriter setDisabled() {
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.setDeactivated();
            }
        }
        return this;
    }

    /**
     * Enables interaction with all buttons.
     */
    public PunchcardWriter setEnabled() {
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.setActive();
            }
        }
        return this;
    }

    /**
     * This  must be called before doing anything otherwise it will cause a {@link NullPointerException}.
     */
    public PunchcardWriter write() {
        this.coordinatesMap = new PunchcardButton[textWriter.getYsize()][textWriter.getXsize()];
        this.allButtons = new HashMap<>(textWriter.getYsize() * textWriter.getXsize());
        for (int i = 1; i < textWriter.getYsize() + 1; i++) {
            for (int j = 1; j < textWriter.getXsize() + 1; j++) {
                this.button = new PunchcardButton(16 * j + xPosition + (5 * 16) + 6, 16 * i + yPosition + 6, 16, 16, this);
                this.addButton(new Point(j - 1, i - 1), button);
                this.screen.addWidget(button);
            }
        }
        syncPositions();
        return this;
    }

    public void tick() {

    }

    /**
     * ({@literal @Deprecated} because the clicking is now handled inside {@link PunchcardButton},
     * this makes it way easier to sychronize between the box and button, it also has way less bugs than before and easier to manage) -
     * <p>
     * Creates a bound between boxes and buttons.
     */
    @Deprecated
    private PunchcardWriter sync() {
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.withCallback(() -> {
                });
            }
        }
        return this;
    }

    /**
     * Performs an action on the specified position.
     */
    public PunchcardWriter modifyAt(Point coordinates, Consumer<PunchcardButton> action) {
        if (coordinatesMap != null && allButtons != null) {
            PunchcardButton button = coordinatesMap[coordinates.y][coordinates.x];
            if (button != null)
                action.accept(button);
        }
        return this;
    }
}
