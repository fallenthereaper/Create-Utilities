package com.fallenreaper.createutilities.core.data.gui.entries;

import com.fallenreaper.createutilities.core.data.gui.BookData;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class BookEntry {
    public ResourceLocation resourceLocation;
    public BookData bookData;


    public BookEntry(JsonObject root, ResourceLocation id, BookData bookData) {
        this.resourceLocation = id;
        this.bookData = bookData;
    }
}
