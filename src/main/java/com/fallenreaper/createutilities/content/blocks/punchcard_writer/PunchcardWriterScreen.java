package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.items.InstructionEntry;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.fallenreaper.createutilities.networking.InventoryEditPacket;
import com.fallenreaper.createutilities.networking.ModPackets;
import com.fallenreaper.createutilities.utils.data.PunchcardWriter;
import com.fallenreaper.createutilities.utils.data.PunchcardWriterManager;
import com.fallenreaper.createutilities.utils.data.SwitchButton;
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
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

//todo: add funnel insert functionality
public class PunchcardWriterScreen extends AbstractSmartContainerScreen<PunchcardWriterContainer> {
    protected static final GuiTextures BG = GuiTextures.PUNCHCARD_WRITER_SCREEN;
    protected static final AllGuiTextures PLAYER = AllGuiTextures.PLAYER_INVENTORY;
    private final ItemStack renderedItem = CUBlocks.PUNCHCARD_WRITER.asStack();
    public IconButton resetButton;
    public LangBuilder lang = Lang.builder(CreateUtilities.ID);
    public PunchcardWriter writer;
    private IconButton closeButton, removeButton, saveButton;
    private List<Rect2i> extraAreas = Collections.emptyList();
    private Vector<Instruction> instructions;
    private ScrollInput optionsInput;
    private Label lineLabel;
    private ScrollInput widthModifier, heightModifier;
    private SwitchButton autosaveSwitch;
    private boolean autosave;

    public PunchcardWriterScreen(PunchcardWriterContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        init();
    }

    public static List<Component> getOptions() {
        List<Component> options = new ArrayList<>();
        SequencerInstructions[] var1 = SequencerInstructions.values();

        int var2 = var1.length;

        for (SequencerInstructions entry : var1) {
            options.add(Lang.translateDirect(entry.toString()));
        }

        return options;
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        var x = leftPos + imageWidth - BG.width;
       var y = topPos;

        BG.render(pPoseStack, x, y, this);
        font.draw(pPoseStack, title, x + 15, y + 3, 0x442000);
        int invX = (leftPos + imageWidth / 4) - 12, invY = y + 199 - 22;

        if (writer != null && !getInventory().getStackInSlot(0).isEmpty()) {
            if (getInventory().getStackInSlot(0).hasTag()) {
                if (getInventory().getStackInSlot(0).getTag().contains("WriterKey")) {
                    if (PunchcardWriterManager.hasWriter(getInventory().getStackInSlot(0).getTag().getString("WriterKey"))) {
                        List<Component> tooltipLines = getInventory().getStackInSlot(0).getTooltipLines(getMenu().player, TooltipFlag.Default.NORMAL);
                        //         tooltipLines.remove(Math.min(writer.getTextWriter().getYsize() + 2, tooltipLines.size()));
                       // tooltipLines.remove(1);
                        tooltipLines.set(0, new TextComponent(" " + "Preview").withStyle(ChatFormatting.GOLD));
                        PoseStack poseStack = new PoseStack();
                        poseStack.pushPose();
                        poseStack.scale(1.25F, 1.25F, 0);
                        poseStack.translate(0, 0, 0);
                        //80
                        this.renderComponentTooltip(poseStack, tooltipLines, (int) (x - 120F), y + 40);
                    }
                }
            }
            //  punchcardWriter.draw(Minecraft.getInstance().font, x / 18f, y, 1.25f, Theme.c(Theme.Key.TEXT_ACCENT_STRONG).scaleAlpha(1f).getRGB());
            var gd = writer.getTextWriter().getCount() / 2;

            writer.renderFillPercentage(Minecraft.getInstance().font, (float) (x + 107 + Math.min(Math.exp(gd) + 0.15, 0.25)), y + 155, 0x404040);
            // this.renderTooltip(pPoseStack, getInventory().getStackInSlot(0), (int) (x/18f), y + 20);
        }
        // renderPlayerInventory(pPoseStack, invX, invY);
        renderScrollWheels(pPoseStack, x, y);
        renderModel(pPoseStack, x + BG.width + 50, y + BG.height + 10, pPartialTick);
        if (!getInventory().getStackInSlot(0).isEmpty() && getInventory().getStackInSlot(0).hasTag()) {
            renderHighlight(pPoseStack, x, y);
        }
    }

    public void renderScrollWheels(PoseStack poseStack, int x, int y) {
        float height = heightModifier.getState();
        float width = widthModifier.getState();
        if (writer != null) {
            height = writer.getHeight();
            width = writer.getWidth();
        }

        //todo  correct positions
        GuiTextures.SCROLL_ICON.render(poseStack, (int) (height + x + 75),  y + 149, this);
        GuiTextures.SCROLL_ICON.render(poseStack, (int) (width + x + 75), y + 161, this);
    }


    @Override
    public void renderPlayerInventory(PoseStack ms, int x, int y) {
        GuiTextures.SINGLE_INVENTORY.render(ms, x, y, this);
        this.font.draw(ms, playerInventoryTitle, x + 8, y + 6, 0x404040);
    }

    protected void renderHighlight(PoseStack matrixStack, int x, int y) {
        GuiTextures.HIGHLIGHT.render(matrixStack, x + 48, y + 28, this);
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

            if (this.hoveredSlot.getItem().getItem() instanceof PunchcardItem item)
                if (this.hoveredSlot.getItem().hasTag() && this.hoveredSlot.getItem().getTag().contains("WriterKey")) {
                    if (this.writer != null)
                        this.writer.renderProgressBar(pX, pY, pPoseStack);
                    // this.renderTooltip(pPoseStack, this.hoveredSlot.getItem(), pX, pY);
                    //   font.drawShadow(pPoseStack, String.valueOf(this.hoveredSlot.getItem().getBarWidth()), pX + 100, pY + 100, Color.GREEN.getRGB());
                }
            if (!(this.hoveredSlot.getItem().getItem() instanceof PunchcardItem))
                this.renderTooltip(pPoseStack, this.hoveredSlot.getItem(), pX, pY);

        }
    }

    private void label(PoseStack ms, int x, int y, Component text) {
        // font.drawShadow(ms, text, guiLeft + x, guiTop + 26 + y, 0xFFFFEE);
    }

    public void addWidget(AbstractWidget widget) {
        this.addRenderableWidget(widget);
    }
//todo: add a block that reads from the card
    @Override
    protected void init() {
        super.init();
        int x = leftPos + imageWidth - BG.width;
        int y = topPos;

        setWindowSize(30 + BG.width, BG.height + PLAYER.height - 35);
        setWindowOffset(-11, 0);

        //Init calls
        readWriter(x, y);
        setButtons();
        setupCallBacks();
        extraAreas = ImmutableList.of(
                new Rect2i(leftPos + 30 + BG.width, topPos + BG.height - 15 - 34 - 6, 72, 68)
        );
    }

    //todo fix this and clean up
    public void initTooltips() {


        resetButton.getToolTip().add(new TextComponent(lang.translate("gui.punchcardwriter.button.reset").string()));
        saveButton.getToolTip().add(new TextComponent(lang.translate("gui.punchcardwriter.button.save").string()));
        removeButton.getToolTip().add(new TextComponent(lang.translate("gui.punchcardwriter.button.remove").string()));
    }

    //todo: add autosaving buttonswitch
    public void setButtons() {
        int x = leftPos + imageWidth - BG.width;
        removeWidgets(removeButton, saveButton, resetButton, heightModifier, widthModifier, closeButton);
        closeButton = new IconButton(leftPos + 30 + BG.width - 33, topPos + BG.height - (42 - 18), AllIcons.I_CONFIRM);
        resetButton = new IconButton(leftPos + 28 + BG.width - 60 * 3, topPos + BG.height - (42 - 18), AllIcons.I_REFRESH);
        removeButton = new IconButton(leftPos + 30 + BG.width - (60 * 3) + 16, topPos + BG.height - (42 - 18), AllIcons.I_TRASH);
        saveButton = new IconButton(leftPos + 32 + BG.width - (60 * 3) + 32, topPos + BG.height - (42 - 18), AllIcons.I_CONFIG_SAVE);
        autosaveSwitch = new SwitchButton(leftPos + 40 + BG.width - (60 * 3) + 32, topPos + BG.height ,18, 18, AllIcons.I_PLAY);
        heightModifier = new ScrollInput(x + 75, topPos + 150, 24, 5).withRange(2, 16)
                .titled(new TextComponent("Height"))
                .setState(2)
                .withShiftStep(16 / 2);
        heightModifier.onChanged();

        widthModifier = new ScrollInput(x + 75, topPos + 161, 24, 5).withRange(2, 16)
                .titled(new TextComponent("Width").append(String.valueOf(this.writer.getWidth())))
                .setState(2)
                .withShiftStep(16 / 2);
        widthModifier.onChanged();


        removeButton.active = false;
        resetButton.active = false;
        saveButton.active = false;
        initTooltips();
        addRenderableWidgets(saveButton, closeButton, resetButton, removeButton, heightModifier, widthModifier, autosaveSwitch);


    }

    private void initGatheringSettings() {
        int x = getGuiLeft();
        int y = getGuiTop();
        lineLabel = new Label(x + 65, y + 70 - 5, net.minecraft.network.chat.TextComponent.EMPTY).withShadow();
        lineLabel.text = new net.minecraft.network.chat.TextComponent(Lang.builder(CreateUtilities.ID).translate("gui.punchcardwriter.scroll_input.title").string());

        optionsInput = new SelectionScrollInput(x + 61, y + 70 - 5, 64 - 4, 16).forOptions(getOptions())
                .writingTo(lineLabel)
                .titled(Lang.builder(CreateUtilities.ID).translate("gui.punchcardwriter.scroll_input.title").component())
                .inverted()
                .calling(i -> {

                } )
                .setState(0);
        optionsInput.onChanged();
        addRenderableWidget(optionsInput);
        addRenderableWidget(lineLabel);
    }


    public void setupCallBacks() {

        closeButton.withCallback(super::onClose);
        //   if (!getInventory().getStackInSlot(0).isEmpty())
        //   punchcardWriter.sync();

        resetButton.withCallback(() -> {
            if (writer != null) {
                writer.fill();
                getBlockEntity().sendData();
            }
            if (!getInventory().getStackInSlot(0).isEmpty())
                if (getInventory().getStackInSlot(0).hasTag())
                    if (getInventory().getStackInSlot(0).getTag().contains("WriterKey"))
                        if (PunchcardWriterManager.hasWriter(getInventory().getStackInSlot(0).getTag().getString("WriterKey"))) {
                            this.writer.fill();
                            PunchcardWriterManager.getWriter(getInventory().getStackInSlot(0).getTag().getString("WriterKey")).fill();
                            getBlockEntity().notifyUpdate();
                        }
        });

        //todo: fix the bug where you have to click multiple times to actually delete the saved writer
        removeButton.withCallback(() -> {
            if (getInventory().getStackInSlot(0).hasTag()) {
                String key = getInventory().getStackInSlot(0).getTag().getString("WriterKey");
                if (getInventory().getStackInSlot(0).getTag().contains("WriterKey") && PunchcardWriterManager.hasWriter(key)) {
                    PunchcardWriterManager.removeWriter(key);

                    ItemStack stack = getInventory().getStackInSlot(0);
                    stack.setTag(null);
                    ModPackets.channel.sendToServer(new InventoryEditPacket(getInventory(), stack));
                    getBlockEntity().notifyUpdate();
                }
            }
        });
        //Will be expanded once I finish the writing system
        saveButton.withCallback(() -> {
            if (!getInventory().getStackInSlot(0).isEmpty()) {
                if (!getInventory().getStackInSlot(0).hasTag()) {
                    saveWriter();
                    System.out.println(writer.toString());
                }
            }
        });
        autosaveSwitch.setState(autosave ? SwitchButton.Mode.OFF : SwitchButton.Mode.ON);

        autosaveSwitch.withCallback(()-> {
            this.autosave = autosaveSwitch.getState() != SwitchButton.Mode.OFF;
        });
        heightModifier.calling((value) -> {
            if (this.writer != null) {
                this.writer.setHeight(value);

            }
        });

        widthModifier.calling((value) -> {
            if (this.writer != null) {
                this.writer.setWidth(value);

            }
        });

    }

    //todo maybe try to use replaceWriter();
    //todo: make it use a list instead of a map
    protected void saveWriter() {
        if (!getInventory().getStackInSlot(0).isEmpty()) {
            ItemStack itemStack = getInventory().getStackInSlot(0);
            List<InstructionEntry> list;
            list = new ArrayList<>();
            var instruction = new InstructionEntry();
           // instruction.instruction = new PunchcardInstruction();
            list.add(instruction);

            CompoundTag tag = itemStack.getOrCreateTag();
            String key = tag.contains("WriterKey") ? tag.getString("WriterKey") : getWriter().getTextWriter().getRawText();
            tag.putString("WriterKey", key);
           // tag.putString("InstructionType", list.get(0).instruction.getLabeledText());
            itemStack.setTag(tag);

            if (PunchcardWriterManager.hasWriter(key)) {
                PunchcardWriterManager.replaceWriter(getWriter(), key);
            } else {
                PunchcardWriterManager.addWriter(getWriter(), key);
            }
            updateSlot(getInventory(), itemStack);
            getBlockEntity().notifyUpdate();

        }
    }
    public static void updateSlot(ItemStackHandler handler, ItemStack itemStack) {
        ModPackets.channel.sendToServer(new InventoryEditPacket(handler, itemStack));
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
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
        var stack = getInventory().getStackInSlot(0);
        boolean hasPunchcard = !stack.isEmpty();

        if (writer != null) {
            if (!hasPunchcard) {
                writer.setDisabled();
            } else {
                writer.setEnabled();
            }
        }

        if(autosaveSwitch != null) {
            autosave = autosaveSwitch.getState() != SwitchButton.Mode.OFF;
        }
        assert autosaveSwitch != null;
        autosaveSwitch.setState(autosave ? SwitchButton.Mode.ON : SwitchButton.Mode.OFF);


            readSavedWriter();

        //   buttonWriter = !getInventory().getStackInSlot(0).isEmpty() && getInventory().getStackInSlot(0).hasTag() ? CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")) : buttonWriter;
        //   writer = getMainBlockEntity().hasPunchcard() && CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")) != null ? CreateUtilities.PUNCHWRITER_NETWORK.savedWriters.get(getInventory().getStackInSlot(0).getTag().getUUID("WriterKey")).getTextWriter() : new PunchcardTextWriter();
        //   writer.writeText(5, 7);

        if (optionsInput != null && lineLabel != null) {
            optionsInput.visible = hasPunchcard;
            optionsInput.active = hasPunchcard;
            lineLabel.visible = hasPunchcard;
            lineLabel.active = hasPunchcard;
        }
        if (!getInventory().getStackInSlot(0).isEmpty()) {
            if (getInventory().getStackInSlot(0).hasTag()) {
                if(autosave) {
                    saveWriter();
                }
            }
        }

        removeButton.active = hasPunchcard && getInventory().getStackInSlot(0).hasTag();
        resetButton.active = hasPunchcard;
        // if (stack.hasTag() && stack.getTag().contains("WriterKey") && punchcardWriter != null && PunchcardWriterManager.hasWriter(stack.getTag().getUUID("WriterKey")))
        //   saveButton.active = !this.punchcardWriter.getTextWriter().equals(PunchcardWriterManager.getWriter(stack.getTag().getUUID("WriterKey")).getTextWriter());
        saveButton.active = hasPunchcard;

        //initTooltips();
        if (getWriter() != null)
            getWriter().sync();

    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    private void readWriter(int x, int y) {
        var stack = getInventory().getStackInSlot(0);
        boolean hasPunchcard = !stack.isEmpty();

        //checks for valid saved writers
        readSavedWriter();
        this.writer = PunchcardWriter.create(this, x, y, 5, 7).write();

        if (!hasPunchcard)
            writer.setDisabled();
        else
            writer.setEnabled();

        readButtons();

        initGatheringSettings();
        if (optionsInput != null && lineLabel != null) {
            optionsInput.visible = hasPunchcard;
            optionsInput.active = hasPunchcard;
            lineLabel.visible = hasPunchcard;
            lineLabel.active = hasPunchcard;
        }
    }

    private void readSavedWriter() {
        ItemStack stack = getInventory().getStackInSlot(0);

        if (!stack.hasTag() || getWriter() == null || !PunchcardWriterManager.hasWriter(stack.getTag().getString("WriterKey")))
            return;

        var savedWriter = PunchcardWriterManager.getWriter(stack.getTag().getString("WriterKey"));
        this.writer.setTextWriter(savedWriter.getTextWriter());
        if (widthModifier != null && heightModifier != null) {
            this.widthModifier.setState(getWriter().getWidth());
            this.heightModifier.setState(getWriter().getHeight());
        }
    }

    private void readButtons() {
        ItemStack stack = getInventory().getStackInSlot(0);
        if (!stack.hasTag() || getWriter() == null || !PunchcardWriterManager.hasWriter(stack.getTag().getString("WriterKey")))
            return;
        PunchcardWriter savedWriter = PunchcardWriterManager.getWriter(stack.getTag().getString("WriterKey"));
        getWriter().updateButtonState(savedWriter);
    }
    private PunchcardWriter getWriter() {


        return this.writer;
    }
}