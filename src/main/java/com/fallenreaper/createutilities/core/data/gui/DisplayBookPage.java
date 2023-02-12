package com.fallenreaper.createutilities.core.data.gui;

import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DisplayBookPage extends BookPage{
    ItemStack itemStack;
    public DisplayBookPage(Component title, ItemStack itemStack) {
        super(title, GuiTextures.DISPLAY_PAGE);
        this.itemStack = itemStack;
    }
@OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y, float pPartialTick, SimpleBookScreen simpleBookScreen) {
        super.render(pPoseStack, pMouseX, pMouseY, x, y, pPartialTick, simpleBookScreen);
        renderItem(pPoseStack, itemStack, x + 20, x + 40, pMouseX, pMouseY, simpleBookScreen );
    }
}
