package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.core.data.gui.BookChapter;
import com.fallenreaper.createutilities.core.data.gui.ClipboardBookPage;
import com.fallenreaper.createutilities.core.data.gui.DisplayBookPage;
import com.fallenreaper.createutilities.core.data.gui.TitlePage;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CUBookChapters {
    public static BookChapter FIRST_CHAPTER;

    public static void register() {
        FIRST_CHAPTER =
                new BookChapter(Component.translatable("gui.createutilities.parchment.first_chapter"),
                        new TitlePage(Component.translatable("gui.createutilities.parchment.first_chapter.title_page")),
                        new ClipboardBookPage(Component.translatable("gui.createutilities.parchment.first_chapter.clipboard_page")),
                        new DisplayBookPage(Component.translatable("gui.createutilities.parchment.first_chapter.display_page"), new ItemStack(CUItems.ENGINEER_TOP_HAT.get())),
                        new TitlePage(Component.translatable("gui.createutilities.parchment.first_chapter.title_page")),
                        new ClipboardBookPage(Component.translatable("gui.createutilities.parchment.first_chapter.clipboard_page")),
                        new DisplayBookPage(Component.translatable("gui.createutilities.parchment.first_chapter.display_page"), new ItemStack(Items.LEATHER)));
    }
}
