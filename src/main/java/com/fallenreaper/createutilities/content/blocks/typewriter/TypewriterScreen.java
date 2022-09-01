package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.items.InstructionManager;
import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.CUItems;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.fallenreaper.createutilities.networking.ModPackets;
import com.fallenreaper.createutilities.networking.TypewriterEditPacket;
import com.fallenreaper.createutilities.utils.data.SwitchButton;
import com.fallenreaper.createutilities.utils.data.SwitchIcons;
import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.container.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TypewriterScreen extends AbstractSimiContainerScreen<TypewriterContainer> {

    protected static final GuiTextures BG = GuiTextures.TYPEWRITER;
    protected static final AllGuiTextures PLAYER = AllGuiTextures.PLAYER_INVENTORY;
    public boolean shouldConsume;
    protected InstructionManager instructionManager;
    protected Indicator clickIndicator;
    private IconButton closeButton;
    private IconButton confirmButton;
    private List<Rect2i> extraAreas = Collections.emptyList();
    private SwitchButton switchButton;

    public TypewriterScreen(TypewriterContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        init();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = leftPos + imageWidth - BG.width;
        int y = topPos + 13;

        BG.render(pPoseStack, x, y, this);
        font.draw(pPoseStack, title, x + 15, y + 3, 0x442000);

        int invX = leftPos;
        int invY = topPos + imageHeight - 3 - PLAYER.height;
        clickIndicator.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        if (!getMainBlockEntity().inventory.getStackInSlot(1).isEmpty() && getMainBlockEntity().inventory.getStackInSlot(1).hasTag()) {
            for (int b : Iterate.positiveAndNegative) {
                if (b == 1) {
                    renderHighlight(pPoseStack, x, y);
                }
            }
        }
        renderPlayerInventory(pPoseStack, invX, invY);
        renderProgressBar(pPoseStack, x, y, getMainBlockEntity().dataGatheringProgress);
        renderModel(pPoseStack, x + BG.width + 50, y + BG.height + 10, pPartialTick);
        renderFuelProgressBar(pPoseStack, x, y, getMainBlockEntity().fuelLevel);

    }

    protected void renderProgressBar(PoseStack matrixStack, int x, int y, float amount) {
        float progress = Math.min(amount, 1);
        GuiTextures sprite = GuiTextures.ARROW_INDICATOR;
        sprite.bind();
        blit(matrixStack, x + 147, y + 52, sprite.startX, sprite.startY, sprite.width, (int) (sprite.height * progress));
    }

    protected void renderFuelProgressBar(PoseStack matrixStack, int x, int y, float progress) {
        GuiTextures sprite = GuiTextures.PROGRESS_BAR;
        sprite.bind();
        blit(matrixStack, x + 37, y + 22, sprite.startX, sprite.startY, (int) (sprite.width * progress), sprite.height);
    }

    protected void renderHighlight(PoseStack matrixStack, int x, int y) {
        GuiTextures.HIGHLIGHT.render(matrixStack, x + 10, y + 56, this);
    }

    protected void renderModel(PoseStack ms, int x, int y, float partialTicks) {

        TransformStack.cast(ms)
                .pushPose()
                .translate(x, y, 100)
                .scale(35)
                .rotateX(-22)
                .rotateY(-202);


        GuiGameElement.of(CUBlocks.TYPEWRITER.getDefaultState())
                .render(ms);

        Slot slot = menu.slots.get(4);
        ItemStack itemstack = slot.getItem();
        GuiGameElement.of(CUItems.PUNCHCARD.get())
                .render(ms);
        if (getMainBlockEntity().hasBlueprintIn()) {

            GuiGameElement.of(CUBlockPartials.SCHEMATIC_MODEL)
                    .render(ms);
        }
        ms.popPose();


        GuiGameElement.of(CUBlocks.TYPEWRITER.asStack())
                .<GuiGameElement.GuiRenderBuilder>at(x - 50, y - 100 + 28, -100)
                .scale(4.5f)
                .render(ms);


    }


    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    protected void init() {
        int x = leftPos;
        int y = topPos;
        setWindowSize(30 + BG.width, BG.height + PLAYER.height - 10);
        setWindowOffset(-11, 0);
        super.init();
        confirmButton = new IconButton(leftPos + 118 + BG.width - 154, topPos + BG.height - 97 - 7, AllIcons.I_PLAY);
        closeButton = new IconButton(leftPos + 30 + BG.width - 33, topPos + BG.height - 22 - 13, AllIcons.I_CONFIRM);

        //   extraAreas = ImmutableList.of(new Rect2i(x + BG.width, y + BG.height + BG.height - 62 - 2, 84, 92 - 2));
        clickIndicator = new Indicator(leftPos + 118 + BG.width - 154, topPos + BG.height - 97 - 14, new TextComponent("Off"));

          switchButton = new SwitchButton(leftPos + 118 + BG.width - 175 , topPos + BG.height - 97,18, 18, SwitchIcons.TYPEWRITER_SWITCHBUTTON) {
              @Override
              protected SoundEvent getLeftClickSound() {
                  return SoundEvents.UI_BUTTON_CLICK;
              }

              @Override
              protected SoundEvent getRightClickSound() {
                  return SoundEvents.UI_BUTTON_CLICK;
              }

              @Override
              protected void onLeftDrag(Point point, double pMouseX, double pMouseY, int pButton) {
              }

              @Override
              protected void onRightDrag(Point point, double pMouseX, double pMouseY, int pButton) {
              }

              @Override
              protected void onLeftClick(double pMouseX, double pMouseY, int pButton) {
              }

              @Override
              protected void onRightClick(double pMouseX, double pMouseY, int pButton) {
              }

              @Override
              protected boolean isValidLeftClickButton(int pButton) {
                  return pButton == 0;
              }

              @Override
              protected boolean isValidRightClickButton(int pButton) {
                  return pButton == 0;
              }
          };
        addRenderableWidget(switchButton);
        addRenderableWidget(closeButton);
        addRenderableWidget(confirmButton);
        addRenderableWidget(clickIndicator);
        boolean hasBlueprint = getMainBlockEntity().hasBlueprintIn();


        extraAreas = ImmutableList.of(
                new Rect2i(leftPos + 30 + BG.width, topPos + BG.height - 15 - 34 - 6, 72, 68)
        );

        clickIndicator.state = Indicator.State.OFF;
        confirmButton.active = false;
        if ((getMainBlockEntity().dataGatheringProgress >= 0.0F))
            clickIndicator.state = Indicator.State.OFF;
        confirmButton.active = false;

        if (getMainBlockEntity().hasBluePrint && getMainBlockEntity().hasFuel && !getMainBlockEntity().inventory.getStackInSlot(1).isEmpty() && getMainBlockEntity().hasFuel && (getMainBlockEntity().dataGatheringProgress <= 1F)) {
            confirmButton.active = true;
            clickIndicator.state = Indicator.State.ON;
        }
        handleTooltips();
        callBacks();
        //  tick();
    }

    protected ItemStackHandler getInventory() {
        return getMainBlockEntity().inventory;
    }

    protected TypewriterBlockEntity getMainBlockEntity() {
        return menu.contentHolder;
    }

    @Override
    protected void containerTick() {
        clickIndicator.state = Indicator.State.OFF;
        confirmButton.active = false;

        if (getMainBlockEntity().hasBluePrint && getMainBlockEntity().hasFuel && !getMainBlockEntity().inventory.getStackInSlot(1).isEmpty()) {
            confirmButton.active = true;
            clickIndicator.state = Indicator.State.ON;
        }
        handleTooltips();
    }

    protected void handleTooltips() {
        boolean enabled = confirmButton.active;
        LangBuilder lang = Lang.builder(CreateUtilities.ID);
        if (enabled) {
            confirmButton.setToolTip(new TextComponent(lang.translate("gui.typewriter.status.show").string()));
        }

    }

    protected void loadData() {
        //TODO, figure out why tf is it not deleting itemstack from the slot, UPDATE: FIXED 7/29/2022, i neeed to use packets
        Item item = menu.slots.get(4).getItem().getItem();
        ItemStack punchcard = getMainBlockEntity().inventory.getStackInSlot(1);
        List<CompoundTag> list;
        boolean loaded;
        //getInventory().getStackInSlot(4).getItem();

        if (punchcard.getItem() instanceof PunchcardItem item1 && getMainBlockEntity().hasBlueprintIn()) {
            if (getMainBlockEntity().hasFuel) {
                if (punchcard.hasTag() && punchcard.getTag().contains("Key")) {

                    CompoundTag tag = punchcard.getTag();
                    list = new ArrayList<>();
                    BlockPos doorPosition = CreateUtilities.DOORLOCK_MANAGER.dataStored.get(tag.getUUID("Key")).blockPos;
                    Block block = getMainBlockEntity().getLevel().getBlockState(doorPosition).getBlock();
                    BlockState blockState = getMainBlockEntity().getLevel().getBlockState(doorPosition);
                    String textInfo = tag.getString("Description");
                    ItemStack note = getMainBlockEntity().inventory.getStackInSlot(4);
                    CompoundTag noteTag = note.getOrCreateTag();


                    list.add(NbtUtils.writeBlockPos(doorPosition));
                    list.add(NbtUtils.writeBlockState(blockState));
                    list.add(tag.getCompound("Description"));

                    noteTag.putString("Description", textInfo);

                    noteTag.put("DoorPosition", NbtUtils.writeBlockPos(doorPosition));

                    getMainBlockEntity().shouldSend();
                    getMainBlockEntity().changeFuelLevel();


                    //   ModPackets.channel.sendToServer(new TypewriterEditPacket(getInventory(), note, noteTag, true));
                    ModPackets.channel.sendToServer(new TypewriterEditPacket(getInventory(), note, noteTag, true));

                    if (getMainBlockEntity().dataGatheringProgress >= 0.9F) {
                        ModPackets.channel.sendToServer(new TypewriterEditPacket(getInventory(), note, noteTag, false));
                        getMainBlockEntity().inventory.setStackInSlot(4, ItemStack.EMPTY);


                        getInventory().setStackInSlot(5, note);
                        getMainBlockEntity().notifyUpdate();

                    }
                }
            }
        }
    }

    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }

    public void callBacks() {
        confirmButton.withCallback(this::loadData);
        closeButton.withCallback(() -> minecraft.player.closeContainer());
    }

}
