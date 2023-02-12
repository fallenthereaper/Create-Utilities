package com.fallenreaper.createutilities.core.data.gui.entries;

import com.fallenreaper.createutilities.core.data.gui.BookData;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class BookItemEntry extends BookEntry {

    public String itemName;

    public BookItemEntry(JsonObject root, ResourceLocation id, BookData bookData) {
        super(root, id, bookData);

    }
}
