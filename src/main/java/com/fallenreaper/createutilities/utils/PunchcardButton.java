package com.fallenreaper.createutilities.utils;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.fallenreaper.createutilities.utils.data.PunchcardWriter;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

public class PunchcardButton extends AbstractSimiWidget {
    public Mode state;
    boolean clicked;
    public PunchcardWriter writer;

    public PunchcardButton(int x, int y, int w, int h, PunchcardWriter writer) {
        super(x, y, w, h);
        this.state = Mode.ACTIVATED;
        this.writer = writer;
    }

    @Override
    public void renderButton(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

            GuiTextures button = clicked || getState() != Mode.ACTIVATED ? GuiTextures.BUTTON_EMPTY
                    : isHoveredOrFocused() ? GuiTextures.BUTTON_HOVER : GuiTextures.BUTTON_FILLED;
            /*
            for (int i = 1; i < writer.getYsize() + 1; i++) {
                int max = i * writer.getXsize();
                int min = Math.max(max - writer.getXsize(), 0);
                int y = 40;
                PoseStack stack = matrixStack;
                drawString(stack, Minecraft.getInstance().font, writer.getRawText().substring(min, max), 200 / 3, (((int) (((9f) * i)) + y)), Theme.c(Theme.Key.BUTTON_HOVER).scaleAlpha(0.25f).getRGB());

            }

             */

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            drawBg(matrixStack, button);

        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(pButton) && getState() == Mode.ACTIVATED) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager(), true);
                    setState(Mode.DEACTIVATED);
                    if (this.writer != null)
                        this.writer.textWriter.setBox(new Point(this.writer.xCoords.get(this.x) - 1, this.writer.yCoords.get(this.y) - 1));
                    this.onClick(pMouseX, pMouseY);
                    return true;
                }
            } else if (isValidRightClickButton(pButton) && getState() == Mode.DEACTIVATED) {
                if (this.clicked(pMouseX, pMouseY)) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager(), false);
                    setState(Mode.ACTIVATED);
                    if (this.writer != null)
                        this.writer.textWriter.fillBox(new Point(this.writer.xCoords.get(this.x) - 1, this.writer.yCoords.get(this.y) - 1));
                    this.onClick(pMouseX, pMouseY);
                    return true;
                }

            }
        }
        return false;
    }

    public void setDeactivated() {
        this.visible = false;
        this.active = false;
    }

    public void setActive() {
        this.active = true;
        this.visible = true;

    }

    public Mode getState() {
        return state;
    }

    public void setState(Mode state) {
        this.state = state;
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return this.active && this.visible && pMouseX >= (double) this.x && pMouseY >= (double) this.y && pMouseX <= (double) (this.x + this.width) && pMouseY <= (double) (this.y + this.height);
    }

    @Override
    protected boolean isValidClickButton(int pButton) {
        return pButton == 0;
    }
    protected boolean isValidRightClickButton(int pButton) {
        return pButton == 1;
    }

    protected void drawBg(PoseStack matrixStack, GuiTextures button) {
        GuiTextures.BUTTON_FILLED.bind();
        blit(matrixStack, x, y, button.startX, button.startY, button.width, button.height);
    }


    public void playDownSound(SoundManager pHandler, boolean rightClick) {
        pHandler.play(SimpleSoundInstance.forUI(rightClick ? SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT : SoundEvents.BOOK_PAGE_TURN, new Random().nextInt(100) < 50 ? 1f : 2f));
    }

    @Override
    public void tick() {
        super.tick();

    }


    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);


    }

    public enum Mode {
        ACTIVATED,
        DEACTIVATED
    }
}
