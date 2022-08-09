package com.fallenreaper.createutilities.utils.data;

import com.fallenreaper.createutilities.content.blocks.punchcard_writer.AbstractSmartContainerScreen;
import com.fallenreaper.createutilities.utils.PunchcardButton;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings("ALL")
public final class PunchcardWriter {
    public PunchcardTextWriter textWriter;
    private AbstractSmartContainerScreen<?> screen;
    private int xPosition;
    private int yPosition;
    private Map<Integer, Integer> yCoords;
    private Map<Integer, Integer> xCoords;
    private PunchcardButton[][] coordinatesMap;
    private String defaultEmpty;
    private String defaultFull;

    /**
     * {@code write()} must called before calling any other method otherwise it will cause a NullPointerException.
     */
    public PunchcardWriter(AbstractSmartContainerScreen<?> screen, int x, int y, int width, int height) {
        this.defaultEmpty = "\u2592";
        this.defaultFull = "\u2588";
        this.textWriter = new PunchcardTextWriter(TextIcon.create(defaultFull, defaultEmpty)).writeText(width, height);
        this.screen = screen;
        this.xPosition = x;
        this.yPosition = y;
    }

    /**
     * Adds a box and a button at the specified position.
     */
    private void addButton(Point position, PunchcardButton button) {
        if (coordinatesMap[1].length <= 0 || coordinatesMap[0].length <= 0)
            return;
        coordinatesMap[position.y][position.x] = button;
    }

    /**
     * Sets a box and a button at the specified position.
     */
    private void setButton(Point position, PunchcardButton.Mode mode) {
        textWriter.setBox(position);
        coordinatesMap[Math.min(coordinatesMap.length - 1, position.y)][Math.min(coordinatesMap[1].length - 1, position.x)].state = mode;
    }

    /**
     * Changes the text icon.
     */
    public PunchcardWriter setIcon(TextIcon icon) {
        textWriter.setIcon(icon);
        return this;
    }

    /**
     * Handles the mapping of every button by attaching it's position to an index.
     */
    private void addPositions() {
        yCoords = new HashMap<>(textWriter.getYsize());
        xCoords = new HashMap<>(textWriter.getXsize());
        for (int y = 1; y < textWriter.getYsize() + 1; y++) {
            for (int x = 1; x < textWriter.getXsize() + 1; x++) {
                PunchcardButton button = coordinatesMap[y - 1][x - 1];
                yCoords.put(button.y, y);
                xCoords.put(button.x, x);
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
                button.state = PunchcardButton.Mode.DEACTIVATED;
                textWriter.setAll();
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
                button.state = PunchcardButton.Mode.ACTIVATED;
                textWriter.fillAll();
            }
        }
        return this;
    }

    /**
     * Renders a box with the specified dimensions.
     */
    public PunchcardWriter draw(Font font, PoseStack matrixStack, int x, int y, int rgb) {
        for (int i = 1; i < this.textWriter.getYsize() + 1; i++) {
            int max = i * this.textWriter.getXsize();
            int min = Math.max(max - textWriter.getXsize(), 0);

            font.drawShadow(matrixStack, this.textWriter.getRawText().substring(min, max), x, ((9 * i) + y), rgb);
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
     * This  must be called before doing anything otherwise it will cause a NullPointerException.
     */
    public PunchcardWriter write() {
        coordinatesMap = new PunchcardButton[textWriter.getYsize()][textWriter.getXsize()];

        for (int i = 1; i < textWriter.getYsize() + 1; i++) {
            for (int j = 1; j < textWriter.getXsize() + 1; j++) {
                //the first calculations are for configuring the spaces between the buttons
                PunchcardButton button = new PunchcardButton((int) (((int) ((103) / 6.5f * j)) + (xPosition + xPosition / 1.627f)), (int) (((int) (22 / (2 - 0.625f) * i)) + yPosition + 6), 16, 16, textWriter);
                this.addButton(new Point(j - 1, i - 1), button);
                screen.addWidget(button);
            }
        }
        addPositions();
        return this;
    }

    public void tick() {
    }

    /**
     * Creates a bound between boxes and buttons.
     */
    public PunchcardWriter sync() {
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];

                button.withCallback(() -> {
                            if (!screen.getInventory().getStackInSlot(0).isEmpty()) {
                                if (yCoords != null && xCoords != null) {
                                    this.setButton(new Point(xCoords.get(button.x) - 1, yCoords.get(button.y) - 1), PunchcardButton.Mode.DEACTIVATED);
                                }
                            }
                        }
                );
            }
        }
        return this;
    }

    /**
     * Performs an action on the specified position.
     */
    public PunchcardWriter modifyAt(int x, int y, BiConsumer<PunchcardButton, PunchcardTextWriter> action) {
        if (coordinatesMap != null) {
            PunchcardButton button = coordinatesMap[Math.min(coordinatesMap.length - 1, y)][Math.min(coordinatesMap[1].length - 1, x)];
            if (button != null)
                action.accept(button, getTextWriter());
        }
        return this;
    }
}
