package com.fallenreaper.createutilities.core.data.gui.file_management;

import com.fallenreaper.createutilities.core.data.gui.BookPage;
import com.google.gson.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.Type;
@OnlyIn(Dist.CLIENT)
public class delw implements JsonSerializer<BookPage> {

    @Override
    public JsonElement serialize(BookPage src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
/*

        public BookPage deserialize(JsonElement src, Type typeOfSrc, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonobject = GsonHelper.convertToJsonObject(src, "book_page");

            String readTextFile = "";
            if (jsonobject.has("background")) {
                readTextFile = GsonHelper.getAsString(jsonobject, "text");
            }

            String title = "";
            if (jsonobject.has("title")) {
                title = GsonHelper.getAsString(jsonobject, "title");
            }

            BookPage page = new BookPage(Component.translatable(jsonobject.get("title").getAsString()),  context.deserialize(jsonobject.get("background"), GuiTextures.class));
            if (jsonobject.has("title")) {
                page.setTitle(Component.Serializer.fromJson(jsonobject));
            }
            return page;
        }

    @Override
    public JsonElement serialize(BookPage src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("title", src.title.getString());
        jsonObject.add("background", context.serialize(src.BG));
        jsonObject.add("bookEntries", context.serialize(src.bookEntries));
        return jsonObject;
    }

    @Override
    public BookChapter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Component title = context.deserialize(jsonObject.get("title"), Component.class);

        BookChapter bookChapter = new BookChapter(title);

        JsonArray pagesArray = jsonObject.getAsJsonArray("pages");
        for (JsonElement pageElement : pagesArray) {
            BookPage page = context.deserialize(pageElement, BookPage.class);
            bookChapter.addBookPage(page);
        }

        return bookChapter;
    }

    @Override
    public JsonElement serialize(BookChapter src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("title", context.serialize(src.chapterTitle));
        jsonObject.add("background", context.serialize(src.BG));
        jsonObject.add("bookEntries", context.serialize(src.bookEntries));
        JsonArray pagesArray = new JsonArray();
        for (BookPage page : src.bookPages) {
            pagesArray.add(context.serialize(page));
            pa
        }
        jsonObject.add("pages", pagesArray);

        return jsonObject;
    }

 */

}
