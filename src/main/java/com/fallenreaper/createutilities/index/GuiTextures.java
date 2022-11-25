package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
@SuppressWarnings("ALL")
public enum GuiTextures implements ScreenElement {

    TYPEWRITER("typewriter_screen", 184, 174),
    ARROW_INDICATOR("typewriter_screen", 216, 44, 8, 24),
    PROGRESS_BAR("typewriter_screen", 47, 162, 46, 12),
    HIGHLIGHT("typewriter_screen", 139, 196, 26, 26),
    PUNCHCARD_WRITER_SCREEN("punchcard_writer_screen", 201, 174),
    BUTTON_FILLED("punchcard_writer_screen", 240, 48, 16, 16),
    BUTTON_EMPTY("punchcard_writer_screen", 208, 48, 16, 16),
    BUTTON_HOVER("punchcard_writer_screen", 240, 64, 16, 16),
    BUTTON_HOVER_EMPTY("punchcard_writer_screen", 208, 64, 16, 16),
    SINGLE_INVENTORY("inventory_gui", 68, 179, 178, 72),

    DEFAULT_BUTTON_FILLED("typewriter_screen", 207, 144, 18, 18),
    DEFAULT_BUTTON_EMPTY("typewriter_screen", 225, 144, 18, 18),
    DEFAULT_BUTTON_HOVER("typewriter_screen", 207, 162, 18, 18),
    DEFAULT_BUTTON_HOVER_EMPTY("typewriter_screen", 225, 162, 18, 18),
    SCROLL_ICON("punchcard_writer_screen", 224, 185, 8, 7);
    public final ResourceLocation location;
    public int width, height;
    public int startX, startY;

    GuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    GuiTextures(String location, int startX, int startY, int width, int height) {
        this(CreateUtilities.ID, location, startX, startY, width, height);
    }

    GuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @OnlyIn(Dist.CLIENT)
    public void bind() {
        RenderSystem.setShaderTexture(0, location);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, int x, int y, GuiComponent component) {
        bind();
        component.blit(ms, x, y, startX, startY, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(ms, c, x, y, startX, startY, width, height);
    }


    @Override
    public void render(PoseStack ms, int x, int y) {
        bind();
        GuiComponent.blit(ms, x, y, 0, startX, startY, width, height, 256, 256);
    }
}
