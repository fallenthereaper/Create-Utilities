package com.fallenreaper.createutilities.utils.data;

import com.fallenreaper.createutilities.content.blocks.punchcard_writer.AbstractSmartContainerScreen;
import com.fallenreaper.createutilities.index.CUConfig;
import com.fallenreaper.createutilities.utils.MathUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.jozufozu.flywheel.repack.joml.Math;
import com.jozufozu.flywheel.repack.joml.Vector2i;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.google.common.base.Strings.repeat;

@SuppressWarnings("all")
public class PunchcardWriter implements IClickable, IDraggable {
    private final AbstractSmartContainerScreen<?> screen;
    public Map<Point, PunchcardButton> allButtons;
    protected Map<Integer, Integer> yCoords, xCoords;
    private String defaultEmpty, defaultFull;
    private PunchcardTextWriter textWriter;
    private PunchcardButton[][] coordinatesMap;
    private PunchcardButton button;
    private SwitchIcon switchIcon;
    private Vector2i position;
    private byte width, height;

    private PunchcardWriter(AbstractSmartContainerScreen<?> screen, int x, int y, byte width, byte height, SwitchIcon switchIcon) {
        this.defaultEmpty = "▒";
        this.defaultFull = "█";
        this.height = height;
        this.width = width;
        this.textWriter = new PunchcardTextWriter(TextIcon.create(defaultFull, defaultEmpty)).writeText(width, height);
        this.screen = screen;
        this.position = new Vector2i(x, y);
        this.switchIcon = switchIcon;
    }

    /**
     * Make sure to call {@link #write()} right after.
     */
    public static PunchcardWriter create(AbstractSmartContainerScreen<?> screen, int x, int y, int width, int height, SwitchIcon icon) {
        return new PunchcardWriter(screen, x, y, (byte) width, (byte) height, icon);
    }

    /**
     * Make sure to call {@link #write()} right after.
     */
    public static PunchcardWriter create(AbstractSmartContainerScreen<?> screen, int x, int y, int width, int height) {
        return new PunchcardWriter(screen, x, y, (byte) width, (byte) height, SwitchIcons.PUNCHCARD_SWITCHBUTTON);
    }

    /**
     * Returns a copy of the specified {@link PunchcardWriter}
     */
    public static PunchcardWriter copy(PunchcardWriter copied$writer) {
        PunchcardWriter copy = new PunchcardWriter(copied$writer.screen, copied$writer.position.x(), copied$writer.position.y(), copied$writer.width, copied$writer.height, copied$writer.switchIcon);
        copy.textWriter = copied$writer.getTextWriter();
        copy.width = copied$writer.width;
        copy.height = copied$writer.height;
        copy.coordinatesMap = copied$writer.coordinatesMap;
        copy.defaultEmpty = copied$writer.defaultEmpty;
        copy.defaultFull = copied$writer.defaultFull;
        copy.xCoords = copied$writer.xCoords;
        copy.yCoords = copied$writer.yCoords;
        copy.position = copied$writer.position;
        return copy;
    }

    public static void writeFile(String fileName, PunchcardWriter writer) {
        ArrayList<PunchcardWriter> files;
        File file = new File(fileName + ".json");
        if (file.exists()) {
            try {
                FileReader reader = new FileReader(file);
                Type type = new TypeToken<ArrayList<PunchcardWriter>>() {
                }.getType();
                Gson gson = new GsonBuilder().registerTypeAdapter(PunchcardWriter.class, new TypeAdapter<PunchcardWriter>() {

                    @Override
                    public void write(JsonWriter out, PunchcardWriter value) throws IOException {
                        out.value(value.getTextWriter().getRawText());
                    }

                    @Override
                    public PunchcardWriter read(JsonReader in) throws IOException {
                        return null;
                    }
                }).create();
                files = gson.fromJson(reader, type);
                reader.close();
                for (PunchcardWriter writerr : files) {
                    System.out.println(writerr.getTextWriter());
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error in creating a FileReader object.");
                ;
            } catch (IOException e) {
                System.err.println("Error in closing the file.");
            }
        } else {
            files = new ArrayList<>();
            files.add(writer);

            try {
                FileWriter fileWriter = new FileWriter(file);
                Gson gson = new Gson();
                gson.toJson(files, fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                System.err.println("Error in writing the File.");
                ;
            }
        }
    }

    /**
     * Returns the fill progress.
     */
    public float getProgress() {
        int total = this.textWriter.getYsize() * this.textWriter.getXsize();
        return Math.min((float) this.textWriter.getCount() / total, 1);
    }

    public Component getProgressBar() {
        int maxSize = this.textWriter.getXsize() * this.textWriter.getYsize();
        String base = "";
        float modifier = getProgress() * maxSize;

        base += ChatFormatting.GRAY + repeat("|", Math.round(modifier) / 2);
        if (getProgress() < 1)
            base += ChatFormatting.DARK_GRAY + repeat("|", Math.round(maxSize - modifier) / 2);

        return new TextComponent(base);
    }

    public float getPercentage() {
        float val = (float) MathUtil.lerp(0, 100, getProgress());
        return val;
    }

    /**
     * Returns the instance of {@link PunchcardTextWriter}.
     */
    public PunchcardTextWriter getTextWriter() {
        return textWriter;
    }

    /**
     * Replaces the current {@link PunchcardTextWriter} with a new one.
     */
    public void setTextWriter(PunchcardTextWriter copy$from) {
        this.textWriter = copy$from;
    }

    /**
     * Adds a box and a button at the specified position.
     */
    private void addButton(Point position, PunchcardButton button) {
        if (coordinatesMap[1].length <= 0 || coordinatesMap[0].length <= 0) return;
        this.coordinatesMap[position.y][position.x] = button;
        this.allButtons.put(position, button);
    }

    /**
     * Sets a box and a button at the specified position.
     */
    public void setBox(Point position) {
        this.textWriter.setPixel(position);
    }

    /**
     * Fill a box and a button at the specified position.
     */
    public void fillBox(Point position) {
        this.textWriter.fillPixel(position);
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
                this.yCoords.put(button.y, y - 1);
                this.xCoords.put(button.x, x - 1);
                this.button.setCallBack(this::onDrag, this::onClick);
                this.button.setClickButton(0, 1);
                this.button.setClickSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundEvents.BOOK_PAGE_TURN);
            }
        }
    }

    /**
     * Instantly disables and sets all boxes.
     */
    public PunchcardWriter set() {
        int value = getTextWriter().getXsize() * getTextWriter().getYsize();
        this.textWriter.add(value - textWriter.getCount());
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.state = SwitchButton.Mode.OFF;
                this.textWriter.set();
            }
        }
        return this;
    }

    /**
     * Instantly fills all boxes.
     */
    public PunchcardWriter fill() {
        int value = getTextWriter().getXsize() * getTextWriter().getYsize();
        this.textWriter.subtract(textWriter.getCount());
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.state = SwitchButton.Mode.ON;
                this.textWriter.fill();
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
                font.drawShadow(poseStack, this.textWriter.getRawText().substring(min, max), x, ((9 * i) + y), rgb);
            }

        return this;
    }

    /**
     * Renders a progress bar.
     */
    public void renderProgressBar(float x, float y, PoseStack poseStack) {
        if (screen == null)
            return;

        final List<Component> progressBar = new ArrayList<>(1);
        progressBar.add(getProgressBar());
        this.screen.renderComponentTooltip(poseStack, progressBar, Math.round(x), Math.round(y));
    }

    /**
     * Renders a fill percentage bar.
     */
    public void renderFillPercentage(Font font, float x, float y, int rgb) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);

        if (getTextWriter() != null)
            font.draw(poseStack, this.getTextWriter().getCount() + "/" + getTextWriter().getXsize() * getTextWriter().getYsize(), x, y, rgb);
    }

    /**
     * Disables interaction with all buttons.
     */
    public PunchcardWriter setDisabled() {
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.setDisabled();
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
                button.setEnabled();
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
                int h = CUConfig.PUNCHCARDWRITER_HEIGHT.get();
                int w = CUConfig.PUNCHCARDWRITER_WIDTH.get();
                double valueH = h / 7F;
                double valueW = w / 5F;
                this.button = new PunchcardButton((int) (16 * j + position.x() + (5 * 16) + 6), (int) (16 * i + position.y() + 6), 16, 16, switchIcon);
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
    public PunchcardWriter addCallBacks(Runnable action) {
        for (PunchcardButton[] punchcardButtons : coordinatesMap) {
            for (int col = 0; col < coordinatesMap[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.withCallback(() -> {
                    action.run();
                });
            }
        }
        return this;
    }

    @Override
    public void onDrag(int mouseX, int mouseY, Point coords, boolean rightClick) {
        this.onClick(mouseX, mouseY, coords, rightClick);
    }

    @Override
    public void onClick(int mouseX, int mouseY, Point coords, boolean rightClick) {
        if (rightClick)
            this.fillBox(new Point(this.xCoords.get(coords.x), this.yCoords.get(coords.y)));
        else
            this.setBox(new Point(this.xCoords.get(coords.x), this.yCoords.get(coords.y)));

    }

    /**
     * Returns a {@link PunchcardButton} at the specified position, Additionally it can also perform an action if the {@link Consumer} is not null.
     */
    public PunchcardButton getButton(Point position, @Nullable Consumer<PunchcardButton> action) {
        if (coordinatesMap != null && allButtons != null) {
            PunchcardButton button = coordinatesMap[Math.min(position.y, this.height)][Math.min(position.x, this.width)];
            if (button != null) {
                if (action != null) {
                    action.accept(button);
                }
                return button;
            }
        }
        return null;
    }

    public void toolTipFormat(List<Component> toolTip, ChatFormatting format, boolean showProgress, int spacing) {
        String space = "";
        for (int i = 0; i < spacing; i++)
            space += " ";

        for (int i = 1; i < this.textWriter.getYsize() + 1; i++) {
            int max = i * this.textWriter.getXsize();
            int min = Math.max(max - this.textWriter.getXsize(), 0);
            toolTip.add(new TextComponent(space + this.textWriter.getRawText().substring(min, max)).withStyle(format));
        }
        if (showProgress)
            toolTip.add(getProgressBar());
    }

    @Override
    public String toString() {
        return "PunchcardWriter[ " +
                "textData = " + this.getTextWriter();

    }
}
