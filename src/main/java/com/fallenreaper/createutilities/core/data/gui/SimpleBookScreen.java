package com.fallenreaper.createutilities.core.data.gui;

import com.fallenreaper.createutilities.index.CUBookChapters;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.io.File;


public abstract class SimpleBookScreen extends Screen {

    protected BookChapter currentChapter;

    protected int currentPageIndex;

    protected BookButton nextButton;

    protected BookButton prevButton;

    protected GuiTextures BG;

    protected ResourceLocation prevPageJSON;

    protected ResourceLocation currentPageJSON;


    /**
     * Instantiates a new Simple book screen.
     *
     * @param pTitle     the p title
     * @param background the background
     */
    public SimpleBookScreen(Component pTitle, GuiTextures background) {
        super(pTitle);
        this.BG = background;
        this.init();
    }


/**
     * Override this if you don't want your book to start with the default chapter and use another one instead.
     */
    protected void setupChapter() {
        String location = "C:/Users/salva/OneDrive/Documentos/GitHub/Create-Utilities/src/main/resources/assets/createutilities/data/book/book_base/book_base.json";
        File chapterFile = new File(location);

      //  if (!chapterFile.exists()) {
            BookChapter.writeChapterToJson(location, CUBookChapters.FIRST_CHAPTER);
     //   }
        if(chapterFile.exists()) {

        }
        this.currentChapter = CUBookChapters.FIRST_CHAPTER;
    }

    private void setupButtons() {
        if(currentChapter == null)
            return;


        this.renderables.clear();
        this.clearWidgets();
        this.prevButton = this.addRenderableWidget(new BookButton(40, 40, 20, 15, GuiTextures.BACK_BUTTON, this));
        this.nextButton = this.addRenderableWidget(new BookButton(60 + 40, 40, 20, 15, GuiTextures.NEXT_BUTTON, this));
        this.prevButton.withCallback(()-> onSwitchPage(false));
        this.nextButton.withCallback(()-> onSwitchPage(true));
        this.prevButton.visible = currentPageIndex > 0;
        this.nextButton.visible = currentPageIndex + 1 < currentChapter.getBookPages().size();
    }

    @Override
    protected void init() {
       super.init();
        playOpenSound();
        setupChapter();
        setupButtons();
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        int x = ((width - BG.width) ) / 2, y = ((height - BG.height)) / 2;
        this.renderBackground(pPoseStack);
        Font font = Minecraft.getInstance().font;
        int size = currentChapter.getBookPages().size()  ;
        int currentPageIndex1 = currentPageIndex + 1;
        String text = currentPageIndex1 + " / " + size;


        BG.render(pPoseStack, x, y, this);


        renderText(pPoseStack, text, x, y);



        BookPage currentPage = currentChapter.getBookPage(currentPageIndex);
        if(prevButton.visible)prevButton.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
        if(nextButton.visible)nextButton.renderButton(pPoseStack, pMouseX, pMouseY, pPartialTick);
        if(currentPage != null) currentPage.render(pPoseStack, pMouseX, pMouseY, x, y, pPartialTick, this);
    }

    public void renderText(PoseStack pPoseStack,String text ,int x, int y) {
        Font font = Minecraft.getInstance().font;
        font.draw(pPoseStack, text ,x + 90, y + 170, 0x442000);
        font.draw(pPoseStack, text ,x + 90, y + 171, 0x442000);
        if(currentChapter.getBookPage(currentPageIndex) != null)
            currentChapter.getBookPage(currentPageIndex).renderText(pPoseStack, text, x, y, this);
    }

    @Override
    public void renderTooltip(PoseStack pPoseStack, ItemStack pItemStack, int x, int y) {
        if (!pItemStack.isEmpty())
            super.renderTooltip(pPoseStack, pItemStack, x, y);
    }

    @Override
    public void tick() {
        super.tick();
        if(prevButton != null && nextButton != null) {
            this.prevButton.visible = currentPageIndex > 0;
            this.nextButton.visible = currentPageIndex + 1 < currentChapter.getBookPages().size();
        }
        BookPage currentPage = currentChapter.getBookPage(currentPageIndex), nextPage = currentChapter.getBookPage(currentPageIndex + 1);
        if(currentPage != null) currentPage.tick();
     //   if(nextPage != null) nextPage.tick();
    }

    private void changeChapter(BookChapter bookChapter) {
        BookChapter prevChapter = this.currentChapter;
        this.onChapterChanged();
        this.currentChapter = bookChapter;
        this.currentPageIndex = 0;
    }


    protected void onChapterChanged() {

        assert minecraft != null;
        this.playDownSound(getMinecraft().getSoundManager());
    }

    /**
     * Play down sound.
     *
     * @param pHandler the p handler
     */
    public void playDownSound(SoundManager pHandler) {

            pHandler.play(SimpleSoundInstance.forUI(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0F));
        }
    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        super.mouseClicked(pMouseX, pMouseY, pButton);
        BookPage currentPage = currentChapter.getBookPage(currentPageIndex);
        if(currentPage != null)
           return currentPage.mouseClicked(pMouseX, pMouseY,pButton, this);


        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public abstract ResourceLocation getRootPage();

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        BookPage currentPage = currentChapter.getBookPage(currentPageIndex);
        if(currentPage != null)
            return currentPage.mouseDragged(pMouseX, pMouseY,pButton, pDragX, pDragY);
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }
/*
    private void onSwitchPage(boolean nextPage) {
        if(nextPage) {
            if(currentPageIndex + 1 < currentChapter.getBookPages().size()) {
                currentPageIndex += 1;
            }
        }
        else {
            if(currentPageIndex > 0) {
                currentPageIndex -= 1;
            }
        }

        this.currentChapter.getBookPage(currentPageIndex).init();
        playOpenSound();

    }

 */

    /**
     * Callback method when the user clicks on the next or previous button.
     *
     * @param next whether to switch to the next page or the previous one
     */
    public void onSwitchPage(boolean next) {
        int nextPageIndex = currentPageIndex + (next ? 1 : -1);
        switchPage(nextPageIndex);
    }

    /**
     * Changes the current page and updates the UI accordingly.
     *
     * @param nextPageIndex the next page index
     */
    public void switchPage(int nextPageIndex) {
        if (nextPageIndex < 0 || nextPageIndex >= currentChapter.getBookPages().size())
            return;
        currentPageIndex = nextPageIndex;
        this.currentChapter.getBookPage(currentPageIndex).init();
        playOpenSound();
        setupButtons();
    }

    protected void playOpenSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
    }

}







