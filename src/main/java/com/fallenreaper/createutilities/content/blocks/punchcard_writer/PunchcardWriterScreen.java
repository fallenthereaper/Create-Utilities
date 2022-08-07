package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.items.data.PunchcardTextWriter;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.fallenreaper.createutilities.networking.ModPackets;
import com.fallenreaper.createutilities.networking.PunchcardWriterEditPacket;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.relays.advanced.sequencer.Instruction;
import com.simibubi.create.content.contraptions.relays.advanced.sequencer.SequencerInstructions;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class PunchcardWriterScreen  extends AbstractSmartContainerScreen<PunchcardWriterContainer> {
    protected static final GuiTextures BG = GuiTextures.PUNCHCARD_WRITER_SCREEN;
    protected static final AllGuiTextures PLAYER = AllGuiTextures.PLAYER_INVENTORY;
    private final ItemStack renderedItem = CUBlocks.PUNCHCARD_WRITER.asStack();
    public PunchcardTextWriter writer;
    public IconButton resetButton;
    public PunchcardTextWriter defaultTextWriter;
    PunchcardWriter buttonWriter;
    PunchcardWriter defaultWriter;
    private IconButton closeButton;
    private IconButton removeButton;
    private IconButton saveButton;
    private List<Rect2i> extraAreas = Collections.emptyList();
    private Vector<Instruction> instructions;
    private ScrollInput optionsInput;
    private Label lineLabel;

    public PunchcardWriterScreen(PunchcardWriterContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        init();
    }

    public static List<Component> getOptions() {
        List<Component> options = new ArrayList();
        SequencerInstructions[] var1 = SequencerInstructions.values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            SequencerInstructions entry = var1[var3];
            options.add(Lang.translateDirect(entry.toString()));
        }

        return options;
    }


    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = leftPos + imageWidth - BG.width;
        int y = topPos;

        BG.render(pPoseStack, x, y, this);
        font.draw(pPoseStack, title, x + 15, y + 3, 0x442000);

        int invX = leftPos;
        int invY = 150 + 6;
        //    renderPlayerInventory(pPoseStack, invX, invY);
        renderModel(pPoseStack, x + BG.width + 50, y + BG.height + 10, pPartialTick);
    }

    protected void renderModel(PoseStack ms, int x, int y, float partialTicks) {

        GuiGameElement.of(renderedItem)
                .<GuiGameElement.GuiRenderBuilder>at(x - 50, y - 100 + 28, -100)
                .scale(4.5f)
                .render(ms);

    }

    private void label(PoseStack ms, int x, int y, Component text) {
        // font.drawShadow(ms, text, guiLeft + x, guiTop + 26 + y, 0xFFFFEE);
    }

    public void addWidget(AbstractWidget widget) {
        addRenderableWidget(widget);
    }

    @Override
    protected void init() {
        super.init();
        int x = leftPos + imageWidth - BG.width;
        int y = topPos;
        setWindowSize(30 + BG.width, BG.height + PLAYER.height - 35);
        setWindowOffset(-11, 0);

        setButtons();
        //TODO, MAKE IT SO IT READS THE PunchcardWriter from the item inside


        defaultWriter = new PunchcardWriter(this, x, y, 5, 7);
        defaultTextWriter = defaultWriter.getTextWriter();

        ItemStack itemStack = getInventory().getStackInSlot(0);
        //  writer = getMainBlockEntity().hasPunchcard() ? CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")).getTextWriter() : defaultTextWriter;


        //  getMainBlockEntity().hasPunchcard() && CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.containsKey(itemStack.getTag().getUUID("WriterKey")) ? CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(itemStack.getTag().getUUID("WriterKey"))
        buttonWriter = new PunchcardWriter(this, x, y, 5, 7);
        buttonWriter.write();
        writer = buttonWriter.getTextWriter();

        if (buttonWriter != null) {
            if (getInventory().getStackInSlot(0).isEmpty()) {
                buttonWriter.setDisabled();
            } else {
                buttonWriter.setEnabled();
            }
        }

        extraAreas = ImmutableList.of(
                new Rect2i(leftPos + 30 + BG.width, topPos + BG.height - 15 - 34 - 6, 72, 68)
        );

        if (!getInventory().getStackInSlot(0).isEmpty())
            initGatheringSettings();

        callBacks();
    }

    public void initTooltips() {

    }

    public void setButtons() {
        closeButton = new IconButton(leftPos + 30 + BG.width - 33, topPos + BG.height - (42 - 18), AllIcons.I_CONFIRM);
        resetButton = new IconButton(leftPos + 28 + BG.width - 60 * 3, topPos + BG.height - (42 - 18), AllIcons.I_REFRESH);
        removeButton = new IconButton(leftPos + 30 + BG.width - (60 * 3) + 16, topPos + BG.height - (42 - 18), AllIcons.I_TRASH);
        saveButton = new IconButton(leftPos + 32 + BG.width - (60 * 3) + 32, topPos + BG.height - (42 - 18), AllIcons.I_CONFIG_SAVE);

        removeButton.active = false;
        resetButton.active = false;
        saveButton.active = false;

        addRenderableWidget(saveButton);
        addRenderableWidget(closeButton);
        addRenderableWidget(resetButton);
        addRenderableWidget(removeButton);
        initTooltips();
    }

    private void initGatheringSettings() {
        int x = getGuiLeft();
        int y = getGuiTop();
        lineLabel = new Label(x + 65, y + 70 - 5, net.minecraft.network.chat.TextComponent.EMPTY).withShadow();
        lineLabel.text = new net.minecraft.network.chat.TextComponent("punchcard_writer.screen.scroll_input");


        optionsInput = new SelectionScrollInput(x + 61, y + 70 - 5, 64 - 4, 16).forOptions(getOptions())
                .writingTo(lineLabel)
                .titled(Lang.builder(CreateUtilities.ID).translate("punchcard_writer.screen.scroll_input").component())
                .inverted()
                .calling(i -> lineLabel.text = new TextComponent("test"))
                .setState(0);
        optionsInput.onChanged();
        addRenderableWidget(optionsInput);
        addRenderableWidget(lineLabel);
    }

    protected ItemStackHandler getInventory() {
        return getBlockEntity().inventory;
    }



    public void callBacks() {

        closeButton.withCallback(() -> minecraft.player.closeContainer());
        buttonWriter.initCallbacks();
        resetButton.withCallback(() -> {
            if (writer != null && buttonWriter != null) {
                buttonWriter.fillAll();
                getBlockEntity().notifyUpdate();

            }
        });
        removeButton.withCallback(() -> {
            if (getBlockEntity().inventory.getStackInSlot(0).hasTag()) {
                if (getInventory().getStackInSlot(0).getTag().contains("WriterKey")) {
                    //  CreateUtilities.DOORLOCK_MANAGER.remove(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey"));
                    CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.remove(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey"));
                    CreateUtilities.PUNCHWRITER_NETWORK.savedWriterText.remove(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey"));
                    ItemStack stack = getInventory().getStackInSlot(0);
                    getInventory().getStackInSlot(0).setTag(null);
                    ModPackets.channel.sendToServer(new PunchcardWriterEditPacket(getInventory(), stack));
                    getBlockEntity().notifyUpdate();
                }
            }
        });
        saveButton.withCallback(() -> {
            if (!getInventory().getStackInSlot(0).isEmpty()) {
                ItemStack itemStack = getInventory().getStackInSlot(0);
                CompoundTag tag = itemStack.getOrCreateTag();
                UUID key = tag.contains("WriterKey") ? tag.getUUID("WriterKey") : UUID.randomUUID();
                tag.putUUID("WriterKey", key);
                itemStack.setTag(tag);
                int x = leftPos + imageWidth - BG.width;
                int y = topPos;

                CreateUtilities.PUNCHWRITER_NETWORK.addWriter(buttonWriter, key);
                CreateUtilities.PUNCHWRITER_NETWORK.add(writer, key);
                ModPackets.channel.sendToServer(new PunchcardWriterEditPacket(getInventory(), itemStack));
                getBlockEntity().notifyUpdate();

            }
        });
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }

    protected void renderGrid(int x, int y) {
        /*
        allButtons = new ArrayList<>();
        allButtons1 = new HashSet<>();
        buttonGrid = new PunchcardButton[writer.getYsize()][writer.getXsize()];
        xCoords = new ArrayList<>(writer.getXsize());
        yCoords = new ArrayList<>(writer.getYsize());


        for (int i = 1; i < writer.getYsize() + 1; i++) {

            for (int j = 1; j < writer.getXsize() + 1; j++) {
                //the first calculations are for configuring the spaces between the buttons

                punchcardButton = new PunchcardButton((int) (((int) ((103) / 6.5f * j)) + (x + x / 1.627f)), (((int) (22 / (2 - 0.625f) * i)) + y + 6), 16, 16, writer);

                    xCoords.add(punchcardButton.x);


                    yCoords.add(punchcardButton.y);

                 buttonGrid[i - 1][j - 1] = punchcardButton;
                addRenderableWidget(punchcardButton);
                allButtons.add(punchcardButton);
                allButtons1.add(punchcardButton);
            }
        }

         */
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    protected boolean checkHotbarKeyPressed(int pKeyCode, int pScanCode) {
        return super.checkHotbarKeyPressed(pKeyCode, pScanCode);
    }

    @Override
    public PunchcardWriterContainer getMenu() {
        return super.getMenu();
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (optionsInput != null && lineLabel != null) {
            optionsInput.active = !getInventory().getStackInSlot(0).isEmpty();
            lineLabel.active = !getInventory().getStackInSlot(0).isEmpty();
        }
        removeButton.active = !getInventory().getStackInSlot(0).isEmpty();
        resetButton.active = !getInventory().getStackInSlot(0).isEmpty() && getInventory().getStackInSlot(0).hasTag();
        saveButton.active = !getInventory().getStackInSlot(0).isEmpty();
        if (buttonWriter != null) {
            if (getInventory().getStackInSlot(0).isEmpty()) {
                buttonWriter.setDisabled();
            } else {
                buttonWriter.setEnabled();
            }
        }
        if (buttonWriter != null)
            buttonWriter.tick();

        if (!getBlockEntity().inventory.getStackInSlot(0).isEmpty() && getInventory().getStackInSlot(0).hasTag()) {

                //   writer = getMainBlockEntity().hasPunchcard() && CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")) != null ? CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")).getTextWriter() : new PunchcardTextWriter();
                //   writer.writeText(5, 7);


        }
    }
}
