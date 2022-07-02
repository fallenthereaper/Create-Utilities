package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.fallenreaper.createutilities.index.CUBlocks;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.fallenreaper.createutilities.utils.ModIcons;
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
        font.draw(pPoseStack, title, x + 15, y + 4, 0x442000);

        int invX = leftPos;
        int invY = 150 - 15;
        clickIndicator.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderPlayerInventory(pPoseStack, invX, invY);
        renderFuelBar(pPoseStack, x, y, te.fuelLevel + pPartialTick);
        renderModel(pPoseStack, x + BG.width + 50, y + BG.height + 10, pPartialTick);


    }

    protected void renderFuelBar(PoseStack matrixStack, int x, int y, float amount) {
        GuiTextures sprite = GuiTextures.ARROW_INDICATOR;
        sprite.bind();
        blit(matrixStack, x + 147, y + 3, sprite.startX, sprite.startY, sprite.width, (int) (sprite.height * amount));
    }

    private void renderModel(PoseStack ms, int x, int y, float partialTicks) {
        TransformStack.cast(ms)
                .pushPose()
                .translate(x, y, 100)
                .scale(45)
                .rotateX(-22)
                .rotateY(-202);


        GuiGameElement.of(CUBlocks.TYPEWRITER.getDefaultState())
                .render(ms);

        Slot slot = menu.slots.get(0);
        ItemStack itemstack = slot.getItem();
        if (!itemstack.isEmpty()) {
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
        confirmButton = new IconButton(leftPos + 118 + BG.width - 121, topPos + BG.height - 65, AllIcons.I_ADD);
        clickIndicator = new Indicator(x + 111, y + 79, TextComponent.EMPTY);
        clickIndicator.state = Indicator.State.RED;
        confirmButton.setIcon(ModIcons.I_CONFIRM);
        confirmButton.withCallback(() -> {
            shouldConsume = true;
        });
        closeButton = new IconButton(leftPos + 30 + BG.width - 33, topPos + BG.height - (42 - 17), AllIcons.I_CONFIRM);
        closeButton.withCallback(() -> {
            minecraft.player.closeContainer();
        });
        addRenderableWidget(closeButton);
        addRenderableWidget(confirmButton);
        addRenderableWidget(clickIndicator);


    }
}
