package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.utils.ContainerBlockEntity;
import com.fallenreaper.createutilities.utils.data.SwitchButton;
import com.simibubi.create.foundation.gui.container.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.container.ContainerBase;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.ItemStackHandler;

public abstract class AbstractSmartContainerScreen<T extends ContainerBase<? extends ContainerBlockEntity<?>>> extends AbstractSimiContainerScreen<T> {

    public AbstractSmartContainerScreen(T container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    public void addWidget(AbstractWidget widget) {
        addRenderableWidget(widget);
    }

    public ItemStackHandler getInventory() {
        return getBlockEntity().inventory;
    }

    protected ContainerBlockEntity<?> getBlockEntity() {
        return menu.contentHolder;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        for (Widget widget : renderables) {
            if(widget instanceof SwitchButton switchButton)
            if (switchButton.isMouseOver(pMouseX, pMouseY)) {
                switchButton.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
                return true;
            }
        }
        return true;
    }

    @Override
    public void renderDirtBackground(int pVOffset) {
        super.renderDirtBackground(pVOffset);
    }




}
