package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.content.items.data.BoxFrame;
import com.fallenreaper.createutilities.content.items.data.PunchcardTextWriter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PunchcardWriter {
    public PunchcardTextWriter writer;
    private Map<Integer, Integer> yCoords;
    private Map<Integer, Integer> xCoords;
    PunchcardWriterScreen screen;
    Point point;
    PunchcardButton button;
    private PunchcardButton[][] dataMap;

    public PunchcardWriter(PunchcardTextWriter writer, PunchcardWriterScreen screen, Point point) {
        this.writer = writer;
        this.screen = screen;
        this.point = point;
    }

    private void addButton(BoxFrame boxFrame, PunchcardButton button) {
        if (dataMap[1].length <= 0 || dataMap[0].length <= 0)
            return;
        dataMap[boxFrame.y][boxFrame.x] = button;
    }

    private void setButton(BoxFrame box) {
        writer.setBox(box);
        dataMap[Math.min(dataMap.length - 1, box.y)][Math.min(dataMap[1].length - 1, box.x)].state = PunchcardButton.Mode.DEACTIVATED;
    }

    private void fillButton(BoxFrame box) {
        writer.fillBox(box);
        dataMap[Math.min(dataMap.length - 1, box.y)][Math.min(dataMap[1].length - 1, box.x)].state = PunchcardButton.Mode.ACTIVATED;
    }

    private void setPositions() {
        yCoords = new HashMap<>(writer.getYsize());
        xCoords = new HashMap<>(writer.getXsize());
        for (int y = 1; y < writer.getYsize() + 1; y++) {
            for (int x = 1; x < writer.getXsize() + 1; x++) {
                PunchcardButton button = dataMap[y - 1][x - 1];
                yCoords.put(button.y, y);
                xCoords.put(button.x, x);
            }
        }
    }

    public void write() {
        dataMap = new PunchcardButton[writer.getYsize()][writer.getXsize()];
        for (int i = 1; i < writer.getYsize() + 1; i++) {
            for (int j = 1; j < writer.getXsize() + 1; j++) {
                //the first calculations are for configuring the spaces between the buttons

                button = new PunchcardButton((int) (((int) ((103) / 6.5f * j)) + (point.getX() + point.getX() / 1.627f)), (int) (((int) (22 / (2 - 0.625f) * i)) + point.getY() + 6), 16, 16, writer);
                this.addButton(new BoxFrame(j - 1, i - 1), button);

                screen.addWidget(button);

            }
        }
        setPositions();
    }

    public void initCallbacks() {
        for (PunchcardButton[] punchcardButtons : dataMap) {
            for (int col = 0; col < dataMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.withCallback(() -> {
                    if (yCoords != null && xCoords != null) {
                        this.setButton(new BoxFrame(xCoords.get(button.x) - 1, yCoords.get(button.y) - 1));

                    }
                }
                );
            }
        }
    }
}
