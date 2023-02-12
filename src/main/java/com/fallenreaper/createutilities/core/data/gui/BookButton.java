package com.fallenreaper.createutilities.core.data.gui;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.utility.Color;
import org.jetbrains.annotations.NotNull;

public class BookButton extends AbstractSimiWidget {
        public GuiTextures buttonTexture;
        public SimpleBookScreen screen;

    protected BookButton(int x, int y, int width, int height, GuiTextures texture, SimpleBookScreen screen) {
        super(x, y, width, height);
        this.buttonTexture = texture;
        this.screen = screen;
    }



    @Override
    public void renderButton(@NotNull PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(ms, mouseX, mouseY, partialTicks);
        drawBg(ms, this.buttonTexture);
    }

    private void drawBg(PoseStack matrixStack, GuiTextures widget) {
        Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        widget.render(matrixStack, this.x, this.y);
        blit(matrixStack, x, y, widget.startX, widget.startY, widget.width, widget.height);
    }
}
