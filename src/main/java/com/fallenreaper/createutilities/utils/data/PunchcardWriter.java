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
    public Map<Integer, Integer> yCoords;
    public Map<Integer, Integer> xCoords;
    public PunchcardButton[][] coordinatesMap;
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
     * {@code write()} must called before calling any other method otherwise it will cause a NullPointerException.
     */
    public static PunchcardWriter create(AbstractSmartContainerScreen<?> screen, int x, int y, int width, int height) {
        return new PunchcardWriter(screen, x, y, width, height);
    }

    /**
     * Creates a copy of the specified {@link PunchcardWriter}
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
        coordinatesMap[position.y][position.x] = button;
    }

    /**
     * Sets a box and a button at the specified position.
     */
    private void setBox(Point position) {
        this.textWriter.setBox(position);
    }

    /**
     * Fill a box and a button at the specified position.
     */
    private void fillBox(Point position) {
        this.textWriter.fillBox(position);
    }

    /**
     * Changes the text icon.
     */
    public PunchcardWriter setIcon(TextIcon icon) {
        this.textWriter.setIcon(icon);
        return this;
    }

    /**
     * Handles the mapping of every button by attaching it's position to an index.
     */
    private void addPositions() {
        this.yCoords = new HashMap<>(textWriter.getYsize());
        this.xCoords = new HashMap<>(textWriter.getXsize());
        for (int y = 1; y < textWriter.getYsize() + 1; y++) {
            for (int x = 1; x < textWriter.getXsize() + 1; x++) {
                PunchcardButton button = coordinatesMap[y - 1][x - 1];
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
                button.state = PunchcardButton.Mode.DEACTIVATED;
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
                button.state = PunchcardButton.Mode.ACTIVATED;
                this.textWriter.fillAll();
            }
        }
        return this;
    }

    /**
     * Renders a box with the specified dimensions.
     */
    public PunchcardWriter draw(Font font, PoseStack matrixStack, int x, int y, int rgb) {
        if (getTextWriter() != null)
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
                PunchcardButton button = new PunchcardButton(16 * j + xPosition + (5 * 16) + 6, 16 * i + yPosition + 6, 16, 16, this);
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
     *(Deprecated because the clicking is now handled inside the {@link PunchcardButton},
     * this makes it way easier to sychronize between the box and button, it also has way less bugs than before and easier to manage) -
     *
     *  Creates a bound between boxes and buttons.
     */
    @Deprecated
    public PunchcardWriter sync() {
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];

                button.withCallback(() -> {
                            // button.setState(button.getState() == PunchcardButton.Mode.ACTIVATED ? PunchcardButton.Mode.DEACTIVATED: PunchcardButton.Mode.ACTIVATED);
                            if (!screen.getInventory().getStackInSlot(0).isEmpty()) {
                                if (yCoords != null && xCoords != null) {
                                    if (button.getState() == PunchcardButton.Mode.ACTIVATED) {
                                    }
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
            PunchcardButton button = coordinatesMap[Math.min(coordinatesMap.length - 1, y - 1)][Math.min(coordinatesMap[1].length - 1, x - 1)];
            if (button != null)
                action.accept(button, getTextWriter());
        }
        return this;
    }
}
