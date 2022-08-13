package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.items.InstructionEntry;
import com.fallenreaper.createutilities.data.PunchcardInstruction;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.fallenreaper.createutilities.networking.ModPackets;
import com.fallenreaper.createutilities.networking.PunchcardWriterEditPacket;
import com.fallenreaper.createutilities.utils.data.PunchcardWriter;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.relays.advanced.sequencer.Instruction;
import com.simibubi.create.content.contraptions.relays.advanced.sequencer.SequencerInstructions;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;
import java.util.*;

public class PunchcardWriterScreen extends AbstractSmartContainerScreen<PunchcardWriterContainer> {
    protected static final GuiTextures BG = GuiTextures.PUNCHCARD_WRITER_SCREEN;
    protected static final AllGuiTextures PLAYER = AllGuiTextures.PLAYER_INVENTORY;
    private final ItemStack renderedItem = CUBlocks.PUNCHCARD_WRITER.asStack();
    public IconButton resetButton;
    public LangBuilder lang = Lang.builder(CreateUtilities.ID);
    public PunchcardWriter punchcardWriter;
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

        int invX = (leftPos + imageWidth / 4) - 12;
        int invY = y + 199 - 22;
        if (punchcardWriter != null && !getInventory().getStackInSlot(0).isEmpty())
            punchcardWriter.draw(Minecraft.getInstance().font, x / 18f, y, 1.25f, Theme.c(Theme.Key.TEXT_ACCENT_STRONG).scaleAlpha(1f).getRGB());

        renderPlayerInventory(pPoseStack, invX, invY);
        renderModel(pPoseStack, x + BG.width + 50, y + BG.height + 10, pPartialTick);
    }


    @Override
    public void renderPlayerInventory(PoseStack ms, int x, int y) {
        GuiTextures.SINGLE_INVENTORY.render(ms, x, y, this);
        this.font.draw(ms, playerInventoryTitle, x + 8, y + 6, 0x404040);
    }

    protected void renderModel(PoseStack ms, int x, int y, float partialTicks) {

        GuiGameElement.of(renderedItem)
                .<GuiGameElement.GuiRenderBuilder>at(x - 50, y - 100 + 28, -100)
                .scale(4.5f)
                .render(ms);

    }

    @Override
    protected void renderTooltip(PoseStack pPoseStack, int pX, int pY) {
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            this.renderTooltip(pPoseStack, this.hoveredSlot.getItem(), pX, pY);
        }
    }

    private void label(PoseStack ms, int x, int y, Component text) {
        // font.drawShadow(ms, text, guiLeft + x, guiTop + 26 + y, 0xFFFFEE);
    }

    @Override
    public int getXSize() {
        return super.getXSize();
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
        readWriter(x, y);
        setButtons();

        extraAreas = ImmutableList.of(
                new Rect2i(leftPos + 30 + BG.width, topPos + BG.height - 15 - 34 - 6, 72, 68)
        );


        callBacks();
    }

    //todo fix this
    public void initTooltips() {
        if (resetButton.isHoveredOrFocused())
            resetButton.setToolTip(new TextComponent(lang.translate("gui.punchcardwriter.button.reset").string()));


        saveButton.setToolTip(new TextComponent(lang.translate("gui.punchcardwriter.button.save").string()));
        removeButton.setToolTip(new TextComponent(lang.translate("gui.punchcardwriter.button.remove").string()));
    }

    public void setButtons() {

        closeButton = new IconButton(leftPos + 30 + BG.width - 33, topPos + BG.height - (42 - 18), AllIcons.I_CONFIRM);
        resetButton = new IconButton(leftPos + 28 + BG.width - 60 * 3, topPos + BG.height - (42 - 18), AllIcons.I_REFRESH);
        removeButton = new IconButton(leftPos + 30 + BG.width - (60 * 3) + 16, topPos + BG.height - (42 - 18), AllIcons.I_TRASH);
        saveButton = new IconButton(leftPos + 32 + BG.width - (60 * 3) + 32, topPos + BG.height - (42 - 18), AllIcons.I_CONFIG_SAVE);

        removeButton.active = false;
        resetButton.active = false;
        saveButton.active = false;
        initTooltips();
        addRenderableWidgets(saveButton, closeButton, resetButton, removeButton);


    }

    private void initGatheringSettings() {
        int x = getGuiLeft();
        int y = getGuiTop();
        lineLabel = new Label(x + 65, y + 70 - 5, net.minecraft.network.chat.TextComponent.EMPTY).withShadow();
        lineLabel.text = new net.minecraft.network.chat.TextComponent("scroll_input");

        optionsInput = new SelectionScrollInput(x + 61, y + 70 - 5, 64 - 4, 16).forOptions(getOptions())
                .writingTo(lineLabel)
                .titled(Lang.builder(CreateUtilities.ID).translate("scroll_input").component())
                .inverted()
                .calling(i -> lineLabel.text = new TextComponent("screen.scroll_input"))
                .setState(0);
        optionsInput.onChanged();
        addRenderableWidget(optionsInput);
        addRenderableWidget(lineLabel);
    }


    public void callBacks() {

        closeButton.withCallback(super::onClose);
        //   if (!getInventory().getStackInSlot(0).isEmpty())
        //   punchcardWriter.sync();

        resetButton.withCallback(() -> {
            if (punchcardWriter != null) {
                punchcardWriter.fillAll();
                getBlockEntity().notifyUpdate();

            }
            if (!getInventory().getStackInSlot(0).isEmpty())
                if (getInventory().getStackInSlot(0).hasTag())
                    if (getInventory().getStackInSlot(0).getTag().contains("WriterKey"))
                        if (CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.containsKey(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")))
                            CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")).fillAll();
            getBlockEntity().notifyUpdate();
        });
        removeButton.withCallback(() -> {
            if (getBlockEntity().inventory.getStackInSlot(0).hasTag()) {
                if (getInventory().getStackInSlot(0).getTag().contains("WriterKey") && CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.containsKey(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey"))) {
                    CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.remove(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey"));
                    CreateUtilities.PUNCHWRITER_NETWORK.savedWriterText.remove(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey"));
                    ItemStack stack = getInventory().getStackInSlot(0);
                    stack.setTag(null);
                    ModPackets.channel.sendToServer(new PunchcardWriterEditPacket(getInventory(), stack));
                    getBlockEntity().notifyUpdate();
                }
            }
        });
        //Will be expanded once I finish the writing system
        saveButton.withCallback(() -> {
            if (!getInventory().getStackInSlot(0).isEmpty()) {
                ItemStack itemStack = getInventory().getStackInSlot(0);
                List<InstructionEntry> list;
                list = new ArrayList<>();
                InstructionEntry instruction = new InstructionEntry();
                instruction.instruction = new PunchcardInstruction();

                list.add(instruction);

                CompoundTag tag = itemStack.getOrCreateTag();
                UUID key = UUID.randomUUID();
                tag.putUUID("WriterKey", key);
                tag.putString("InstructionType", list.get(0).instruction.getLabeledText());
                itemStack.setTag(tag);
                CreateUtilities.PUNCHWRITER_NETWORK.addWriter(punchcardWriter, key);
                ModPackets.channel.sendToServer(new PunchcardWriterEditPacket(getInventory(), itemStack));
                getBlockEntity().notifyUpdate();

            }
        });
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }

    //
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

        assert this.minecraft != null;
        Minecraft.getInstance().setScreen(new CreativeModeInventoryScreen(this.minecraft.player));
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        ItemStack stack = getInventory().getStackInSlot(0);

        if (punchcardWriter != null) {
            if (stack.isEmpty()) {
                punchcardWriter.setDisabled();
            } else {
                punchcardWriter.setEnabled();
            }
        }
        readFromSavedWriter();
        ///   buttonWriter = !getInventory().getStackInSlot(0).isEmpty() && getInventory().getStackInSlot(0).hasTag() ? CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")) : buttonWriter;
        //   writer = getMainBlockEntity().hasPunchcard() && CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")) != null ? CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")).getTextWriter() : new PunchcardTextWriter();
        //   writer.writeText(5, 7);

        if (optionsInput != null && lineLabel != null) {
            optionsInput.visible = !getInventory().getStackInSlot(0).isEmpty();
            optionsInput.active = !getInventory().getStackInSlot(0).isEmpty();
            lineLabel.visible = !getInventory().getStackInSlot(0).isEmpty();
            lineLabel.active = !getInventory().getStackInSlot(0).isEmpty();
        }

        removeButton.active = !getInventory().getStackInSlot(0).isEmpty() && getInventory().getStackInSlot(0).hasTag();
        resetButton.active = !getInventory().getStackInSlot(0).isEmpty();
        if (!getInventory().getStackInSlot(0).isEmpty()) {
            saveButton.active = true;
            if (stack.hasTag() && stack.getTag().contains("WriterKey") && punchcardWriter != null)
                saveButton.active = !this.punchcardWriter.getTextWriter().equals(CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(stack.getTag().getUUID("WriterKey")).getTextWriter());
        } else
            saveButton.active = false;


        //initTooltips();
        if (punchcardWriter != null)
            punchcardWriter.tick();

    }

    private void readWriter(int x, int y) {
        ItemStack stack = getInventory().getStackInSlot(0);

        readFromSavedWriter();

        this.punchcardWriter = PunchcardWriter.create(this, x, y, 2, 2).write();
        //TODO, try to check if this button Mode is the same as the one saved, update: didn't work at all
        if (stack.isEmpty()) {
            punchcardWriter.setDisabled();
        } else {
            punchcardWriter.setEnabled();
        }

        readButtons();
        initGatheringSettings();
        if (optionsInput != null && lineLabel != null) {
            optionsInput.visible = !getInventory().getStackInSlot(0).isEmpty();
            optionsInput.active = !getInventory().getStackInSlot(0).isEmpty();
            lineLabel.visible = !getInventory().getStackInSlot(0).isEmpty();
            lineLabel.active = !getInventory().getStackInSlot(0).isEmpty();
        }
    }

    private void readFromSavedWriter() {
        ItemStack stack = getInventory().getStackInSlot(0);

        if (!stack.hasTag() || this.punchcardWriter == null || !CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.containsKey(stack.getTag().getUUID("WriterKey")))
            return;

        PunchcardWriter savedWriter = CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(stack.getTag().getUUID("WriterKey"));

        this.punchcardWriter.textWriter = savedWriter.getTextWriter();


    }

    private void readButtons() {
        ItemStack stack = getInventory().getStackInSlot(0);
        if (!stack.hasTag() || this.punchcardWriter == null || !CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.containsKey(stack.getTag().getUUID("WriterKey")))
            return;

        PunchcardWriter savedWriter = CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(stack.getTag().getUUID("WriterKey"));

        for (int i = 1; i < this.punchcardWriter.getTextWriter().getYsize() + 1; i++) {
            for (int j = 1; j < this.punchcardWriter.getTextWriter().getXsize() + 1; j++) {

                // this.punchcardWriter.coordinatesMap[cols][col].state = CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(stack.getTag().getUUID("WriterKey")).coordinatesMap[cols][col].state;

                this.punchcardWriter.allButtons.get(new Point(j - 1, i - 1)).state = savedWriter.allButtons.get(new Point(j - 1, i - 1)).state;
                savedWriter.allButtons.get(new Point(j - 1, i - 1)).state = this.punchcardWriter.allButtons.get(new Point(j - 1, i - 1)).state;

                //  this.punchcardWriter.button.state = CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(stack.getTag().getUUID("WriterKey")).button.state;
            }
        }
    }
}