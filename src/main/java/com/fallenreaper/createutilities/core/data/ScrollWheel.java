package com.fallenreaper.createutilities.core.data;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.utility.Color;
import org.jetbrains.annotations.NotNull;

public class ScrollWheel extends AbstractSimiWidget {
    public int stepValue;
    public int min, max;
    public GuiTextures wheel;

    public ScrollWheel(int x, int y, int width, int height, GuiTextures wheel) {
        super(x, y, width, height);
        this.wheel = wheel;
    }

    @Override
    public void renderButton(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

           // this.drawBg(matrixStack);

        }
    }

    private void drawBg(PoseStack matrixStack) {
        Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      //  wheel.bind();
       // wheel.render(matrixStack, x + 30, stepValue  );
        blit(matrixStack, x, stepValue , wheel.startX, wheel.startY, wheel.width, wheel.height);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

        if (isHoveredOrFocused()) {

          float s = (float) ( ((pMouseY - y  ) / (float) height));
          stepValue = (int) Math.min(104, Math.round((s * 104))) ;
            System.out.println(stepValue);
            System.out.println("mouse " + pMouseY);
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        if (isHoveredOrFocused()) {

            float s = (float) ( ((pMouseY - y  ) / (float) height));
            stepValue = (int) Math.min(88, Math.round((s * 88))) ;
            System.out.println(stepValue);
            System.out.println("mouse " + pMouseY);
        }
        return super.clicked(pMouseX, pMouseY);

    }


    @Override
    public boolean isHoveredOrFocused() {
        return super.isHoveredOrFocused();
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return super.isMouseOver(pMouseX, pMouseY);
    }
}
