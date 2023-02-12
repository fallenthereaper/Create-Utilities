package com.fallenreaper.createutilities.core.data.gui;

import com.fallenreaper.createutilities.core.data.gui.entries.BookEntry;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a book page, which extends the GuiComponent class.
 * A book page consists of a background texture (GuiTextures), a title (Component), and a list of book entries (List<BookEntry>).
 * This class also provides methods for rendering the page, handling mouse events, and getting the background texture.
 */
public class BookPage extends GuiComponent {
    /**
     * The background texture for the book page
     */
    protected GuiTextures BG;

    /**
     * The title of the book page
     */
    public Component title;

    /**
     * The list of book entries in the page
     */
    public List<BookEntry> bookEntries;

    /**
     * Creates a new instance of the `BookPage` class
     * @param title The title of the book page
     * @param BG The background texture for the book page
     */
    public BookPage(Component title, GuiTextures BG) {
        this.BG = BG;
        this.title = title;
        this.bookEntries = new ArrayList<>();
        this.init();
        List<? extends BookEntry> bookEntries = new ArrayList<>();
        this.addBookEntry(bookEntries);
        this.bookEntries.addAll(bookEntries);
    }

    /**
     * Renders an item in the book screen
     * @param pPoseStack The pose stack used for rendering
     * @param pItemStack The item to render
     * @param x The x-coordinate of the item
     * @param y The y-coordinate of the item
     * @param mouseX The x-coordinate of the mouse
     * @param mouseY The y-coordinate of the mouse
     * @param bookScreen The book screen instance
     */
    @OnlyIn(Dist.CLIENT)
    public static void renderItem(PoseStack pPoseStack, ItemStack pItemStack, int x, int y, int mouseX, int mouseY, SimpleBookScreen bookScreen) {
        if (pItemStack != null) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            itemRenderer.renderAndDecorateItem(pItemStack, x, y);
            itemRenderer.renderGuiItemDecorations(Minecraft.getInstance().font, pItemStack, x, y, null);
            if (mouseX >= x && mouseY >= y && mouseX <= x + 16 && mouseY <= y + 16) {
                bookScreen.renderTooltip(pPoseStack, pItemStack, mouseX, mouseY);
            }
        }
    }


    /**
     * Adds book entries to the page
     * @param bookEntries The book entries to add
     */
    protected void addBookEntry(List<? extends BookEntry> bookEntries) {}

    /**
     * Initializes the book page
     */
    protected void init() {}

    /**

     Ticks this book page. This method can be overridden by subclasses to perform any necessary updates.
     */
    public void tick() {}

/**

 Renders this book page on the screen.
 @param pPoseStack the stack of poses representing the transformation matrix to apply
 @param pMouseX the x position of the mouse
 @param pMouseY the y position of the mouse
 @param x the x position on the screen
 @param y the y position on the screen
 @param pPartialTick the partial tick time
 **/
    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y, float pPartialTick, SimpleBookScreen simpleBookScreen) {
        renderBg(pPoseStack, pMouseX, pMouseY, x, y, pPartialTick, simpleBookScreen);
        simpleBookScreen.getMinecraft().font.draw(pPoseStack, title, x, y, 0xff_000000);

    }

    @OnlyIn(Dist.CLIENT)
    public void renderBg(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y, float pPartialTick, SimpleBookScreen screen) {

        if (BG != null)   BG.render(pPoseStack, x + 20, y + 41, screen);
    }

    //todo
    public boolean mouseClicked(double mouseX, double mouseY, int button, SimpleBookScreen screen) {
        return false;
    }



    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiTextures getBackgroundTexture() {
        return BG;
    }

    @OnlyIn(Dist.CLIENT)
    public void renderText(PoseStack pPoseStack, String text, int x, int y, SimpleBookScreen screen) {}


    //Cre



    public void setTitle(Component title) {
        this.title = title;
    }
}
