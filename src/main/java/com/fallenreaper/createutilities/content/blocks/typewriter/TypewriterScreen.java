package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.schematics.block.SchematicannonContainer;
import com.simibubi.create.foundation.gui.container.AbstractSimiContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TypewriterScreen extends AbstractSimiContainerScreen<TypewriterContainer> {

    public TypewriterScreen(TypewriterContainer container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {

    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    protected void init() {
        super.init();
    }
}
