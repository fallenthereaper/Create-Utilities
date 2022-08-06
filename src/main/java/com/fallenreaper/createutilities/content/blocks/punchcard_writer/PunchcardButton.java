package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.content.items.data.BoxFrame;
import com.fallenreaper.createutilities.content.items.data.PunchcardWriter;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class PunchcardButton extends AbstractSimiWidget {
    boolean clicked;
    boolean mode;
    Mode state;
    PunchcardWriter writer;

    public PunchcardButton(int x, int y, int w, int h, PunchcardWriter punchcardWriter) {
        super(x, y, w, h);
        this.writer = punchcardWriter;
    }



    @Override
    public void renderButton(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            isHovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

            GuiTextures button = clicked || !active || getState() == Mode.DEACTIVATED || getState() != Mode.ACTIVATED  ? GuiTextures.BUTTON_EMPTY
                    : isHoveredOrFocused() ? GuiTextures.BUTTON_HOVER : GuiTextures.BUTTON_FILLED;



            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            drawBg(matrixStack, button);

        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(pButton)) {
                boolean flag = this.clicked(pMouseX, pMouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    setState(Mode.DEACTIVATED);
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

    public void setState(Mode state) {
        this.state = state;
    }

    public Mode getState() {
        return state;
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return this.active && this.visible && pMouseX >= (double)this.x && pMouseY >= (double)this.y && pMouseX <= (double)(this.x + this.width) && pMouseY <= (double)(this.y + this.height);
    }

    @Override
    protected boolean isValidClickButton(int pButton) {
        return pButton == 0 || pButton == 1;
    }

    protected void drawBg(PoseStack matrixStack, GuiTextures button) {
      //  GuiTextures.BUTTON_FILLED.bind();
        blit(matrixStack, x, y, button.startX, button.startY, button.width, button.height);
    }

    @Override
    public void playDownSound(SoundManager pHandler) {
        pHandler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 2.0F));
    }

    @Override
    public <T extends AbstractSimiWidget> T withCallback(BiConsumer<Integer, Integer> cb) {

        return super.withCallback(cb);
    }

    @Override
    public <T extends AbstractSimiWidget> T withCallback(Runnable cb) {
        return super.withCallback(cb);
    }

    @Override
    public void tick() {
       super.tick();

        }


    @Override
    public void onClick(double mouseX, double mouseY) {
    writer.setBox(new BoxFrame(0, 2));
 //    this.clicked = true;



    }


    @Override
    public void render(@NotNull PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        super.render(ms, mouseX, mouseY, partialTicks);
    }
    public enum Mode {
        ACTIVATED,
        DEACTIVATED
    }
}
