package com.fallenreaper.createutilities.utils.data;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

@SuppressWarnings("ALL")
public abstract class SwitchButton extends AbstractSimiWidget {
    protected Mode state;
    SwitchIcon switchIcon;

    protected SwitchButton(int x, int y, int width, int height, SwitchIcon switchIcon) {
        super(x, y, width, height);
        setState(Mode.ON);
        this.switchIcon = switchIcon;
    }

    public void playDownSound(SoundManager pHandler, boolean leftClick) {
        pHandler.play(SimpleSoundInstance.forUI(leftClick ? getLeftClickSound() : getRightClickSound(), new Random().nextInt(100) < 50 ? 1.0F : 2.0F, new Random().nextInt(100) < 50 ? 0.25F : 0.35F));
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return this.active && this.visible && pMouseX >= (double) this.x && pMouseY >= (double) this.y && pMouseX <= (double) (this.x + this.width) && pMouseY <= (double) (this.y + this.height);
    }

    protected abstract SoundEvent getLeftClickSound();

    protected abstract SoundEvent getRightClickSound();

    @Override
    public void renderButton(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            this.drawBg(matrixStack, isHoveredOrFocused() && isDeactivated() ? switchIcon.getHoveredEmpty() : findWidgetTexture());
        }
    }

    private void drawBg(PoseStack matrixStack, GuiTextures widget) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        widget.bind();
        blit(matrixStack, x, y, widget.startX, widget.startY, widget.width, widget.height);
    }

    private GuiTextures findWidgetTexture() {
        if (!active || isDeactivated()) {
            return switchIcon.getEmpty();
        } else if (isHoveredOrFocused() && isActivated()) {
            return switchIcon.getHovered();
        }

        return switchIcon.getFilled();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.visible) {
            if (isValidLeftClickButton(pButton) && getState() == Mode.ON) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager(), true);
                    setState(Mode.OFF);
                    this.onClick(pMouseX, pMouseY);
                    this.onLeftClick(pMouseX, pMouseY, pButton);
                    return true;
                }
            } else if (isValidRightClickButton(pButton) && getState() == Mode.OFF) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager(), false);
                    setState(Mode.ON);
                    this.onClick(pMouseX, pMouseY);
                    this.onRightClick(pMouseX, pMouseY, pButton);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        if (isValidLeftClickButton(pButton) && isActivated()) {
            onLeftDrag(new Point(this.x, this.y), pMouseX, pMouseY, pButton);
            this.onDrag(pMouseX, pMouseY, pDragX, pDragY);
            this.setState(Mode.OFF);
            return true;
        } else if (isValidRightClickButton(pButton) && isDeactivated()) {
            onRightDrag(new Point(this.x, this.y), pMouseX, pMouseY, pButton);
            this.onDrag(pMouseX, pMouseY, pDragX, pDragY);
            this.setState(Mode.ON);
            return true;
        } else {
            return false;
        }
    }

    protected abstract void onLeftDrag(Point point, double pMouseX, double pMouseY, int pButton);

    protected abstract void onRightDrag(Point point, double pMouseX, double pMouseY, int pButton);

    public boolean isActivated() {
        return getState() == Mode.ON;
    }

    public boolean isDeactivated() {
        return getState() == Mode.OFF;
    }

    protected abstract void onLeftClick(double pMouseX, double pMouseY, int pButton);

    protected abstract void onRightClick(double pMouseX, double pMouseY, int pButton);

    protected abstract boolean isValidLeftClickButton(int pButton);

    protected abstract boolean isValidRightClickButton(int pButton);

    public void setDisabled() {
        this.visible = false;
        this.active = false;
    }

    public void setEnabled() {
        this.active = true;
        this.visible = true;
    }

    public Mode getState() {
        return state;
    }

    public void setState(Mode state) {
        this.state = state;
    }

    public enum Mode {
        ON,
        OFF
    }
}
