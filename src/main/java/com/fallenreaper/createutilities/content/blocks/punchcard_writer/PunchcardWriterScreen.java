package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.items.data.PunchcardTextWriter;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.relays.advanced.sequencer.Instruction;
import com.simibubi.create.content.contraptions.relays.advanced.sequencer.SequencerInstructions;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.container.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.awt.*;
import java.util.List;
import java.util.*;

public class PunchcardWriterScreen extends AbstractSimiContainerScreen<PunchcardWriterContainer> {
    public PunchcardTextWriter writer;
    private final ItemStack renderedItem = CUBlocks.PUNCHCARD_WRITER.asStack();
    protected static final GuiTextures BG = GuiTextures.PUNCHCARD_WRITER_SCREEN;
    protected static final AllGuiTextures PLAYER = AllGuiTextures.PLAYER_INVENTORY;
    PunchcardButton punchcardButton;
    private IconButton closeButton;
    private List<Rect2i> extraAreas = Collections.emptyList();
    private Vector<Instruction> instructions;
    private ScrollInput optionsInput;
    private Label lineLabel;
    PunchcardWriter buttonWriter;

    public PunchcardWriterScreen(PunchcardWriterContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        init();

    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = leftPos + imageWidth - BG.width;
        int y = topPos;

        BG.render(pPoseStack, x, y, this);
        font.draw(pPoseStack, title, x + 15, y + 3, 0x442000);

        int invX = leftPos;
        int invY = 150 + 6;
        renderPlayerInventory(pPoseStack, invX, invY);
        renderModel(pPoseStack, x + BG.width + 50, y + BG.height + 10, pPartialTick);
    }

    protected void renderModel(PoseStack ms, int x, int y, float partialTicks) {
        TransformStack.cast(ms)
                .pushPose()
                .translate(x, y - 40/2, 100)
                .scale(45)
                .rotateX(-45F)
                .rotateY(-225);


        GuiGameElement.of(CUBlocks.PUNCHCARD_WRITER.getDefaultState())
                .render(ms);
        ms.popPose();



    }
    private void label(PoseStack ms, int x, int y, Component text) {
      //  font.drawShadow(ms, text, guiLeft + x, guiTop + 26 + y, 0xFFFFEE);
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

        closeButton = new IconButton(leftPos + 30 + BG.width - 33, topPos + BG.height - (42 - 18), AllIcons.I_CONFIRM);
        addRenderableWidget(closeButton);


                  writer = new PunchcardTextWriter();
                  writer.writeText(5, 7);

                  buttonWriter = new PunchcardWriter(writer, this, new Point(x, y));
                  buttonWriter.write();
                //  buttonWriter.addPositions();

                //  renderGrid(x, y);




        extraAreas = ImmutableList.of(
                new Rect2i(leftPos + 30 + BG.width, topPos + BG.height - 15 - 34 - 6, 72, 68)
        );
        /*
        if(!(allButtons == null)) {
            for (PunchcardButton button : allButtons) {
                button.setDeactivated();
            }}

        if(getMainBlockEntity().hasPunchcard()) {
            if(!(allButtons == null))
                for (PunchcardButton button : allButtons) {
                    button.withCallback(()-> {
                        button.state = button.getState() == PunchcardButton.Mode.DEACTIVATED ? PunchcardButton.Mode.ACTIVATED : PunchcardButton.Mode.DEACTIVATED;
                        System.out.println("ETEE");
                    });
                }
            getMainBlockEntity().notifyUpdate();
            initGatheringSettings();
        }

         */
            callBacks();
    }
     public   static List<Component> getOptions() {
        List<Component> options = new ArrayList();
        SequencerInstructions[] var1 = SequencerInstructions.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            SequencerInstructions entry = var1[var3];
            options.add(Lang.translateDirect(entry.toString(), new Object[0]));
        }

        return options;
    }
    private void initGatheringSettings() {
        int x = getGuiLeft();
        int y = getGuiTop();
        lineLabel = new Label(x + 65, y + 70-5, net.minecraft.network.chat.TextComponent.EMPTY).withShadow();
        lineLabel.text = new net.minecraft.network.chat.TextComponent("test2");


        optionsInput = new SelectionScrollInput(x + 61, y + 70-5, 64  - 4, 16).forOptions(getOptions())
                .writingTo(lineLabel)
                .titled( Lang.builder(CreateUtilities.ID).translate("punchcard_writer.screen.scroll_input").component())
                .inverted()
                .calling(i -> lineLabel.text = new TextComponent("test"))
                .setState(0);
        optionsInput.onChanged();
        addRenderableWidget(optionsInput);
        addRenderableWidget(lineLabel);
    }
    protected ItemStackHandler getInventory() {
        return getMainBlockEntity().inventory;
    }
    protected PunchcardWriterBlockEntity getMainBlockEntity() {
        return menu.contentHolder;
    }

    public void callBacks() {

        closeButton.withCallback(() -> minecraft.player.closeContainer());
        buttonWriter.initCallbacks();
        /*
        if (!(allButtons == null)) {
            for (PunchcardButton button : allButtons) {
                button.withCallback(() -> writer.setBox(new BoxFrame(Mth.clamp(xCoords.indexOf(button.x), 0, 5), Mth.clamp(yCoords.indexOf(button.y), 0, 7))));
                getMainBlockEntity().notifyUpdate();
                System.out.println(allButtons.indexOf(button));
            }
        }

         */
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
        /*
        if (!(allButtons == null)) {
            for (PunchcardButton button : allButtons) {
                button.setDeactivated();
            }

            if (getMainBlockEntity().hasPunchcard()) {
                if (!(allButtons == null))
                    for (PunchcardButton button : allButtons) {
                        button.setActive();
                    }

         */
    }
}
