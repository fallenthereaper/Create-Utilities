package com.fallenreaper.createutilities.utils.data;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

@SuppressWarnings("ALL")
public class SwitchButton extends AbstractSimiWidget {
    protected Mode state;
    protected SwitchIcon switchIcon;
    protected ScreenElement icon;
    protected boolean hasIcon;
    protected IClickable runClick = ($$, $_, $$_, $) -> {};
    protected IDraggable runDrag = ($$, $_, $$_, $) -> {};
    protected SoundEvent leftSound = SoundEvents.UI_BUTTON_CLICK;
    protected SoundEvent rightSound = SoundEvents.UI_BUTTON_CLICK;
    protected int rightClickButton = 0;
    protected int leftClickButton = 0;

    public SwitchButton(int x, int y, int width, int height, SwitchIcon switchIcon) {
        super(x, y, width, height);
        setState(Mode.ON);
        this.switchIcon = switchIcon;
    }

    public SwitchButton(int x, int y, int width, int height, SwitchIcon switchIcon, ScreenElement icon) {
        this(x, y, width, height, switchIcon);
        this.icon = icon;
        hasIcon = true;
    }

    public SwitchButton(int x, int y, int width, int height, ScreenElement icon) {
        this(x, y, width, height, SwitchIcons.SWITCHBUTTON, icon);
    }

    public void playDownSound(SoundManager pHandler, boolean leftClick) {
        pHandler.play(SimpleSoundInstance.forUI(leftClick ? this.leftSound : this.rightSound, 1.0F, new Random().nextInt(100) < 50 ? 0.75F : 1.0F));
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return this.active && this.visible && pMouseX >= (double) this.x && pMouseY >= (double) this.y && pMouseX <= (double) (this.x + this.width) && pMouseY <= (double) (this.y + this.height);
    }

    @Override
    public void renderButton(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

            this.drawBg(matrixStack, isHoveredOrFocused() && getState() != Mode.ON ? switchIcon.getHoveredEmpty() : findWidgetTexture());
            if (hasIcon) {
                icon.render(matrixStack, x + 1, y + 1);
            }
        }
    }

    private void drawBg(PoseStack matrixStack, GuiTextures widget) {
        Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        widget.bind();
        blit(matrixStack, x, y, widget.startX, widget.startY, widget.width, widget.height);
    }

    private GuiTextures findWidgetTexture() {
        if (!active || getState() != Mode.ON) {
            return switchIcon.getEmpty();
        } else if (isHoveredOrFocused() && getState() != Mode.OFF) {
            return switchIcon.getHovered();
        }
        else
        return switchIcon.getFull();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.visible) {
            if (isValidLeftClickButton(pButton) && getState() == Mode.ON) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager(), true);
                    this.onClick(pMouseX, pMouseY);
                    this.runClick.onClick((int) pMouseX, (int) pMouseY, new Point(this.x, this.y), false);
                    setState(Mode.OFF);
                    return true;
                }
            } else if (isValidRightClickButton(pButton) && getState() == Mode.OFF) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager(), false);
                    this.onClick(pMouseX, pMouseY);
                    this.runClick.onClick((int) pMouseX, (int) pMouseY, new Point(this.x, this.y), true);
                    setState(Mode.ON);
                    return true;
                }
            }
        }
        return false;
    }

    public void setCallBack(IDraggable _$_, IClickable $$_) {
        this.runDrag = _$_;
        this.runClick = $$_;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {

        if (isValidLeftClickButton(pButton) && getState() != Mode.OFF) {
            this.playDownSound(Minecraft.getInstance().getSoundManager(), true);
            this.runDrag.onDrag((int) pMouseX, (int) pMouseY, new Point(this.x, this.y), false);
            this.setState(Mode.OFF);
            return true;
        } else if (isValidRightClickButton(pButton) && getState() != Mode.ON) {
            this.playDownSound(Minecraft.getInstance().getSoundManager(), false);
            this.runDrag.onDrag((int) pMouseX, (int) pMouseY, new Point(this.x, this.y), true);
            this.setState(Mode.ON);
            return true;
        } else {
            return  super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    protected boolean isValidLeftClickButton(int pButton) {
        return pButton == this.leftClickButton;
    }

    ;

    protected boolean isValidRightClickButton(int pButton) {
        return pButton == this.rightClickButton;
    }

    ;

    public void setDisabled() {
        this.visible = false;
        this.active = false;
    }

    public void setEnabled() {
        this.active = true;
        this.visible = true;
    }

    public void setClickSound(SoundEvent leftClick, SoundEvent rightClick) {
        this.leftSound = leftClick;
        this.rightSound = rightClick;
    }

    public void setClickButton(int leftClickButton, int rightClickButton) {
        this.rightClickButton = rightClickButton;
        this.leftClickButton = leftClickButton;
    }

    public Mode getState() {
        return state;
    }

    public void setState(Mode state) {
        this.state = state;
    }

    public void setSwitchIcon(SwitchIcon icon) {
        this.switchIcon = icon;
    }

    public enum Mode {
        ON,
        OFF
    }
}
