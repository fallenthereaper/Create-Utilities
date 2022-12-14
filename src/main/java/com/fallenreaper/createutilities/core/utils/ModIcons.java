package com.fallenreaper.createutilities.core.utils;

import com.fallenreaper.createutilities.CreateUtilities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;

public class ModIcons extends AllIcons {
    public static final ResourceLocation ICON_ATLAS = CreateUtilities.defaultResourceLocation("textures/gui/typewriter_screen.png");

    public ModIcons(int x, int y) {
        super(x, y);
    }

    @Override
    public void bind() {
        super.bind();
    }

    @Override
    public void render(PoseStack matrixStack, int x, int y) {
        render(matrixStack, x, y);
    }

    @Override
    public void render(PoseStack matrixStack, int x, int y, GuiComponent component) {
        super.render(matrixStack, x, y, component);
    }

    @Override
    public void render(PoseStack ms, MultiBufferSource buffer, int color) {
        super.render(ms, buffer, color);
    }

    @Override
    public DelegatedStencilElement asStencil() {
        return super.asStencil();
    }
}
