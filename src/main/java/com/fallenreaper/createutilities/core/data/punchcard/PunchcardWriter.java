package com.fallenreaper.createutilities.core.data.punchcard;

import com.fallenreaper.createutilities.content.blocks.punchcard_writer.AbstractSmartContainerScreen;
import com.fallenreaper.createutilities.core.data.Interactable;
import com.fallenreaper.createutilities.core.data.*;
import com.fallenreaper.createutilities.core.utils.MiscUtil;
import com.fallenreaper.createutilities.index.CUConfig;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.base.Strings.repeat;

@SuppressWarnings("all")
public class PunchcardWriter implements Interactable.IClickable, Interactable.IDraggable {
    private AbstractSmartContainerScreen<?> screen;
    private final SwitchIcon switchIcon;
    public Map<Point, PunchcardButton> allButtons;
    protected Map<Integer, Integer> yCoords, xCoords;
    private String defaultEmpty, defaultFull;
    private PunchcardTextWriter textWriter;
    private PunchcardButton[][] grid;
    private PunchcardButton button;
    private Vector2i position;
    private byte width, height;

    private PunchcardWriter(AbstractSmartContainerScreen<?> screen, int x, int y, byte width, byte height, SwitchIcon switchIcon, TextIcon icon) {
        this.defaultEmpty = icon.getEmptyIcon();
        this.defaultFull = icon.getFullIcon();
        this.height = height;
        this.width = width;
        this.textWriter = new PunchcardTextWriter(TextIcon.of(defaultFull, defaultEmpty), width, height).writeText();
        this.screen = screen;
        this.position = new Vector2i(x, y);
        this.switchIcon = switchIcon;
    }

    /**
     * Make sure to call {@link #write()} right after.
     */
    public static PunchcardWriter create(AbstractSmartContainerScreen<?> screen, int x, int y, int width, int height, SwitchIcon icon, TextIcon textIcon) {
        return new PunchcardWriter(screen, x, y, (byte) width, (byte) height, icon, textIcon);
    }

    /**
     * Make sure to call {@link #write()} right after.
     */
    public static PunchcardWriter create(AbstractSmartContainerScreen<?> screen, int x, int y, int width, int height) {
        return new PunchcardWriter(screen, x, y, (byte) width, (byte) height, SwitchIcons.PUNCHCARD_SWITCHBUTTON, TextIcon.of("█", "▒" ));
    }

    /**
     * Returns a copy of the specified {@link PunchcardWriter}
     */
    public static PunchcardWriter copy(PunchcardWriter copied$writer) {
        PunchcardWriter copy = new PunchcardWriter(copied$writer.screen, copied$writer.position.x(), copied$writer.position.y(), copied$writer.width, copied$writer.height, copied$writer.switchIcon, TextIcon.of(copied$writer.defaultEmpty, copied$writer.defaultFull));
        copy.textWriter = copied$writer.getTextWriter();
        copy.width = copied$writer.width;
        copy.height = copied$writer.height;
        copy.grid = copied$writer.grid;
        copy.defaultEmpty = copied$writer.defaultEmpty;
        copy.defaultFull = copied$writer.defaultFull;
        copy.xCoords = copied$writer.xCoords;
        copy.yCoords = copied$writer.yCoords;
        copy.position = copied$writer.position;
        return copy;
    }

    public static PunchcardWriter read(CompoundTag compoundTag) {
        return null;
    }

    public static void write(CompoundTag compoundTag) {

    }
    public void setScreen(AbstractSmartContainerScreen<?> screen) {
        this.screen = screen;
    }
    public AbstractSmartContainerScreen<?> getScreen() {
        return screen;
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

    public byte getWidth() {
        return this.textWriter.getXsize();
    }

    public void setWidth(int width) {
        this.width = (byte) width;
        this.textWriter.setWidth(width);
    }

    /**
     * Returns the current height.
     */
    public byte getHeight() {
        return this.textWriter.getYsize();
    }

    /**
     * Sets the height to a new one.
     */
    public void setHeight(int height) {
        this.height = (byte) height;
        this.textWriter.setHeight(height);
    }

    /**
     * Updates every button state.
     */
    public void updateButtonState(PunchcardWriter target$writer) {
        for (int height = 1; height < this.getHeight() + 1; height++) {
            for (int width = 1; width < this.getWidth() + 1; width++) {
                var saved = target$writer.getButton(new Point(width - 1, height - 1), null);
                this.getButton(new Point(width - 1, height - 1),
                        button ->
                                button.setState(saved.getState())
                );
            }
        }
    }

    public void write(CompoundTag compoundTag, boolean client) {
        this.getTextWriter().write(compoundTag, client);

    }
    public void read(CompoundTag compoundTag, boolean client) {
        this.getTextWriter().read(compoundTag, client);
    }

    /**
     * Returns the fill progress.
     */
    public float getProgress() {
        int total = this.textWriter.getYsize() * this.textWriter.getXsize();
        return Math.min((float) this.textWriter.getCount() / total, 1);
    }

    protected Component getProgressBar() {
        int maxSize = this.textWriter.getXsize() * this.textWriter.getYsize();
        var base = "";
        float modifier = getProgress() * maxSize;
        float fullValue = (modifier / maxSize) * 16;
        float remaining = ((maxSize * (1 - getProgress())) / maxSize) * 16;

        base += ChatFormatting.GRAY + repeat("|", Math.round(fullValue));

        if (getProgress() < 1)
            base += ChatFormatting.DARK_GRAY + repeat("|", Math.round(remaining));

        return new TextComponent(base);
    }

    protected float getPercentage() {
        return (float) MiscUtil.lerp(0, 100, getProgress());
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
        if (grid[1].length == 0 || grid[0].length == 0) return;
        this.grid[position.y][position.x] = button;
        this.allButtons.put(position, button);
    }

    private void removeButton(Point position, PunchcardButton button) {
        if (grid[1].length == 0 || grid[0].length == 0) return;
        List<PunchcardButton[]> arrayList = Stream.of(grid).toList();


        allButtons.remove(position);
        this.allButtons.remove(position, button);
    }
    public static int[] removeTheElement(int[] arr, int index)
    {

        // If the array is empty
        // or the index is not in array range
        // return the original array
        if (arr == null
                || index < 0
                || index >= arr.length) {

            return arr;
        }

        // Create ArrayList from the array
        List<Integer> arrayList = IntStream.of(arr)
                .boxed()
                .collect(Collectors.toList());

        // Remove the specified element
        arrayList.remove(index);

        // return the resultant array
        return arrayList.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    /**
     * Sets a box and a button at the specified position.
     */
    public void setBox(Point position, boolean rightClick) {
        if(rightClick)
            this.textWriter.fillPixel(position);
        else
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
    public PunchcardWriter setIcon(TextIcon<String, String> icon) {
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
                this.button = grid[y - 1][x - 1];
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
        for (PunchcardButton[] punchcardButtons : grid) {
            for (int col = 0; col < grid[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.setState(SwitchButton.Mode.OFF);
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
        this.textWriter.fill();
        for (PunchcardButton[] punchcardButtons : grid) {
            for (int col = 0; col < grid[1].length; col++) {
                PunchcardButton button = punchcardButtons[col];
                button.setState(SwitchButton.Mode.ON);
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
        poseStack.translate(0, 0, 0);
        int per = (int) Math.floor(getPercentage());
        if (getTextWriter() != null)
            font.draw(poseStack, this.getTextWriter().getCount() + "/" + getTextWriter().getXsize() * getTextWriter().getYsize(), x, y, rgb);
        font.draw(poseStack, per + "%", x + 20, y + 20, rgb);
    }

    /**
     * Disables interaction with all buttons.
     */
    public PunchcardWriter setDisabled() {
        for (PunchcardButton[] punchcardButtons : grid) {
            for (int col = 0; col < grid[1].length; col++) {
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
        for (PunchcardButton[] punchcardButtons : grid) {
            for (int col = 0; col < grid[1].length; col++) {
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
        this.grid = new PunchcardButton[textWriter.getYsize()][textWriter.getXsize()];
        this.allButtons = new HashMap<>(textWriter.getYsize() * textWriter.getXsize());
        for (int i = 1; i < textWriter.getYsize() + 1; i++) {
            for (int j = 1; j < textWriter.getXsize() + 1; j++) {
                int h = CUConfig.PUNCHCARDWRITER_HEIGHT.get();
                int w = CUConfig.PUNCHCARDWRITER_WIDTH.get();
                this.button = new PunchcardButton((int) (16 * j + position.x() + (5 * 16) + 6), (int) (16 * i + position.y() + 6), 16, 16, switchIcon);
                this.addButton(new Point(j - 1, i - 1), button);
                this.screen.addWidget(button);
            }
        }
        syncPositions();
        return this;
    }

    public PunchcardWriter clear() {
        for (int height = 1; height < this.getHeight() + 1; height++) {
            for (int width = 1; width < this.getWidth() + 1; width++) {
                PunchcardButton buttons = this.getButton(new Point(width - 1, height - 1), null);
                screen.removeWidget(buttons);
                removeButton(new Point(width - 1, height - 1), buttons);
            }
        }
        return this;
    }

    public void sync() {

    }

    /**
     * ({@literal @Deprecated} because the clicking is now handled inside {@link PunchcardButton},
     * this makes it way easier to sychronize between the box and button, it also has way less bugs than before and easier to manage) -
     * <p>
     * Creates a bound between boxes and buttons.
     */
    @Deprecated
    public PunchcardWriter addCallBacks(Runnable action) {
        for (PunchcardButton[] punchcardButtons : grid) {
            for (int col = 0; col < grid[1].length; col++) {
                var button = punchcardButtons[col];
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
            this.setBox(new Point(this.xCoords.get(coords.x), this.yCoords.get(coords.y)), rightClick);
    }

    /**
     * Returns a {@link PunchcardButton} at the specified position, Additionally it can also perform an action if specified a {@link Consumer}.
     */
    public final PunchcardButton getButton(Point position, @Nullable Consumer<PunchcardButton> actions) {
        if (grid != null && allButtons != null) {
            PunchcardButton button = grid[Math.min(position.y, this.height)][Math.min(position.x, this.width)];
            if (button != null) {
                if (actions != null) {
                    actions.accept(button);
                }
                return button;
            }
        }
        return null;
    }

    public void toolTipFormat(List<Component> toolTip, boolean showProgress, int spacing, ChatFormatting... format) {
        var space = "";
        for (int i = 0; i < spacing; i++)
            space += " ";

        for (int i = 1; i < this.textWriter.getYsize() + 1; i++) {
            var max = i * this.textWriter.getXsize();
            var min = Math.max(max - this.textWriter.getXsize(), 0);
            toolTip.add(new TextComponent(space + this.textWriter.getRawText().substring(min, max)).withStyle(format));
        }
        if (showProgress)
            toolTip.add(getProgressBar());
    }

    @Override
    public String toString() {
        return "PunchcardWriter[" +
                "textData = " + this.getTextWriter().getRawText() +
                " x = " + position.x() +
                " y = " + position.y() +
                " fillPercentage = " + (int) getPercentage() +
                " boxAmount = " + getWidth() * getHeight() +
                "]";
    }
}
