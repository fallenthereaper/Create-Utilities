package com.fallenreaper.createutilities.content.items.parchment;

import com.fallenreaper.createutilities.core.data.ScrollWheel;
import com.fallenreaper.createutilities.core.data.gui.SimpleBookScreen;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ParchmentScreen extends SimpleBookScreen {
      public ScrollWheel scrollWheel;
      public GuiTextures slider;

    public ParchmentScreen() {
        super(Component.translatable("gui.createutilities.parchment.title"), GuiTextures.PARCHMENT_BACKGROUND);
    }

    @Override
    protected void init() {
        super.init();
  //      int x = leftPos + imageWidth - BG.width;
 //       int y = topPos;
        this.slider = GuiTextures.SCROLL_WHEEL;
        int x = ((width - BG.width) ) / 2, y = ((height - BG.height)- 60) / 2;

    //    setWindowSize(30 + BG.width, BG.height + PLAYER.height - 35);
      //  setWindowOffset(-11, 0);
        this.scrollWheel = new ScrollWheel(x + 136, y + 189/2, 6, 110, GuiTextures.SCROLL_WHEEL);
        addRenderableWidget(this.scrollWheel);

    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        renderBg(pPoseStack, pMouseX, pMouseY, pPartialTick);

    }

    private void renderBg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {

        int x = ((width - BG.width) ) / 2, y = ((height - BG.height)- 60) / 2;
      //  BG.render(pPoseStack, x, y, this);
        ///   BACKGROUND.bind();
        ///   blit(pPoseStack , x, y, BACKGROUND.startX, BACKGROUND.startY, BACKGROUND.width, BACKGROUND.height);

       this.slider = pMouseX >= scrollWheel.x && pMouseX <= scrollWheel.x + scrollWheel.getWidth() && pMouseY >= scrollWheel.y && pMouseY <= scrollWheel.y + scrollWheel.getHeight() ? GuiTextures.SCROLL_WHEEL_HOVER : GuiTextures.SCROLL_WHEEL;

        slider.render(pPoseStack, (int) (this.scrollWheel.x + 1),   this.scrollWheel.stepValue + 41, this);



      //  BACKGROUND.bind();

        font.draw(pPoseStack, title, x, y + 233 , 0x442000);

    }
//todo, make a slider widget class and start working on  the parchment slider
    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public ResourceLocation getRootPage() {
        return null;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        if (scrollWheel.isHoveredOrFocused()) {

        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
