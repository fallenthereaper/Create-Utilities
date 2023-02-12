package com.fallenreaper.createutilities.core.data.gui.file_management;

import com.fallenreaper.createutilities.core.data.gui.BookChapter;
import com.fallenreaper.createutilities.core.data.gui.BookPage;
import com.google.gson.*;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookChapterLoader {

    public static class BookDeserializer implements JsonDeserializer<BookChapter> {
        @Override
        public BookChapter deserialize(JsonElement src, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = src.getAsJsonObject();
            JsonArray bookPagesArray = jsonObject.get("bookPages").getAsJsonArray();
            List<BookPage> bookPages = new ArrayList<>();
            for (JsonElement bookPageElement : bookPagesArray) {
                bookPages.add(context.deserialize(bookPageElement, BookPage.class));
            }
            Component chapterTitle = Component.translatable(jsonObject.get("chapterTitle").getAsString());
            return new BookChapter(chapterTitle, bookPages.toArray(new BookPage[0]));
        }
    }
    public static class BookSerializer implements JsonSerializer<BookChapter> {

        @Override
        public JsonElement serialize(BookChapter src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            JsonArray bookPagesArray = new JsonArray();
            for (BookPage bookPage : src.getBookPages()) {
                bookPagesArray.add(context.serialize(bookPage));
            }
            jsonObject.add("bookPages", bookPagesArray);
            jsonObject.add("chapterTitle", context.serialize(src.getChapterTitle().getString()));
            return jsonObject;
        }
    }
}
