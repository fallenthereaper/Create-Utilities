package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.core.data.SwitchButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin extends Screen {
    protected AbstractContainerScreenMixin(Component pTitle) {
        super(pTitle);
    }


    @Inject(method = "mouseDragged", at = @At(value = "HEAD"), require = 0)
    public void createutilities_mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY, CallbackInfoReturnable<Boolean> cir) {
        for (Widget widget : renderables) {
            if(widget instanceof SwitchButton switchButton) {
                if (switchButton.isMouseOver(pMouseX, pMouseY)) {
                    switchButton.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
                }
            }
        }
    }
}
