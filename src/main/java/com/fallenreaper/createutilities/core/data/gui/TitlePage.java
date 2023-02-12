package com.fallenreaper.createutilities.core.data.gui;

import com.fallenreaper.createutilities.core.data.gui.entries.BookEntry;
import com.fallenreaper.createutilities.index.GuiTextures;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TitlePage extends BookPage {
    public TitlePage(Component title) {
        super(title, GuiTextures.TITLE_PAGE);
    }

    @Override
    protected void addBookEntry(List<? extends BookEntry> bookEntries) {
        super.addBookEntry(bookEntries);

    }


}
