package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.container.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


public class TypewriterScreen extends AbstractSimiContainerScreen<TypewriterContainer> {
    protected static final GuiTextures BG = GuiTextures.TYPEWRITER;
    protected static final AllGuiTextures PLAYER = AllGuiTextures.PLAYER_INVENTORY;

    private IconButton closeButton;
    private IconButton confirmButton;
    protected Indicator clickIndicator;

    public boolean shouldConsume;


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
        int y = topPos;
        TypewriterBlockEntity te = menu.contentHolder;
        BG.render(pPoseStack, x, y, this);
        font.draw(pPoseStack, title, x + 15, y + 3, 0x442000);

        int invX = leftPos;
        int invY = 150 - 15;
        clickIndicator.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderPlayerInventory(pPoseStack, invX, invY);
        renderFuelBar(pPoseStack, x, y, te.fuelLevel);
        renderModel(pPoseStack, x + BG.width + 50, y + BG.height + 10, pPartialTick);

    }

    protected void renderFuelBar(PoseStack matrixStack, int x, int y, float amount) {
        GuiTextures sprite = GuiTextures.ARROW_INDICATOR;
       sprite.startY = 0;
        sprite.bind();
        blit(matrixStack, x + 147, y + 8, sprite.startX, sprite.startY, sprite.width, (int) (sprite.height * amount));
    }

    protected void renderModel(PoseStack ms, int x, int y, float partialTicks) {
        TransformStack.cast(ms)
                .pushPose()
                .translate(x, y, 100)
                .scale(45)
                .rotateX(-22)
                .rotateY(-202);


        GuiGameElement.of(CUBlocks.TYPEWRITER.getDefaultState())
                .render(ms);

        Slot slot = menu.slots.get(4);
        ItemStack itemstack = slot.getItem();
        if (menu.contentHolder.hasBlueprintIn()) {
            GuiGameElement.of(CUBlockPartials.SCHEMATIC_MODEL)
                    .render(ms);
        }
        ms.popPose();


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
        confirmButton = new IconButton(leftPos + 118 + BG.width - 154, topPos + BG.height - 91+4, AllIcons.I_PLAY);
        clickIndicator = new Indicator(leftPos + 118 + BG.width - 154, topPos + BG.height - 98+4, new TextComponent("Off"));
        clickIndicator.state = Indicator.State.OFF;
        confirmButton.active = false;

   if(menu.contentHolder.hasBlueprintIn()) {
       confirmButton.active = true;
       menu.contentHolder.notifyUpdate();

   }
     callBacks();


        closeButton = new IconButton(leftPos + 30 + BG.width - 33, topPos + BG.height - (42 - 17), AllIcons.I_CONFIRM);
        closeButton.withCallback(() -> {
            minecraft.player.closeContainer();
        }); closeButton.withCallback(() -> {
            minecraft.player.closeContainer();
        });
        addRenderableWidget(closeButton);
        addRenderableWidget(confirmButton);
        addRenderableWidget(clickIndicator);

    }
    public void callBacks() {
        confirmButton.withCallback(() -> {

        });

    }

}
