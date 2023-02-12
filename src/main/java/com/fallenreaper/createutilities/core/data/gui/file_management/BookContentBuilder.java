package com.fallenreaper.createutilities.core.data.gui.file_management;

import com.fallenreaper.createutilities.core.data.gui.BookChapter;
import com.fallenreaper.createutilities.core.data.gui.BookPage;
import com.fallenreaper.createutilities.core.data.gui.entries.BookEntry;
import com.fallenreaper.createutilities.index.GuiTextures;
import com.google.gson.*;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;


public class BookContentBuilder {

        public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting()
                .registerTypeAdapter(BookChapterLoader.BookSerializer.class, new BookChapterLoader.BookSerializer())
                .setPrettyPrinting()
                .registerTypeAdapter(BookChapterLoader.BookDeserializer.class, new BookChapterLoader.BookDeserializer())
                .create();

        public static class BookChapterSerializer implements JsonSerializer<BookChapter>, JsonDeserializer<BookPage>  {

            @Override
            public JsonElement serialize(BookChapter src, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject chapter = new JsonObject();
                chapter.addProperty("chapter_title", src.getChapterTitle().getString());
                JsonArray pages = new JsonArray();
                for (BookPage page : src.getBookPages()) {
                    pages.add(context.serialize(page));
                }
                chapter.add("pages", pages);
                return chapter;
            }

            @Override
            public BookPage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return null;
            }
        }

        public static class BookPageSerializer implements JsonSerializer<BookPage>, JsonDeserializer<BookPage> {
            public BookPage deserialize(JsonElement src, Type typeOfSrc, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonobject = GsonHelper.convertToJsonObject(src, "book_page");

                String readTextFile = "";
                if (jsonobject.has("background")) {
                    readTextFile = GsonHelper.getAsString(jsonobject, "background");
                }

                String title = "";
                if (jsonobject.has("page_title")) {
                    title = GsonHelper.getAsString(jsonobject, "page_title");
                }

                BookPage page = new BookPage(Component.translatable(jsonobject.get("title").getAsString()),  context.deserialize(jsonobject.get("background"), GuiTextures.class));
                if (jsonobject.has("page_title")) {
                    page.setTitle(Component.Serializer.fromJson(jsonobject));
                }
                return page;
            }

            @Override
            public JsonElement serialize(BookPage src, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject page = new JsonObject();
                page.addProperty("page_title", src.title.getString());
                page.add("background", context.serialize(src.getBackgroundTexture()));
                JsonArray entries = new JsonArray();
                if(src.bookEntries.size() > 0) {
                    for (BookEntry entry : src.bookEntries) {
                        entries.add(context.serialize(entry));
                    }
                }
                page.add("data", entries);

                return page;
            }


        }

        public static class GuiTextureSerializer implements JsonSerializer<GuiTextures>, JsonDeserializer<GuiTextures>  {

            @Override
            public JsonElement serialize(GuiTextures src, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject object = new JsonObject();
                object.addProperty("path", src.location.getPath());
                return object;
            }

            @Override
            public GuiTextures deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return null;
            }
        }

        public static void writeChapterToJson(BookChapter chapter, File file) {
            try (FileWriter writer = new FileWriter(file)) {
                GSON.toJson(chapter, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static BookChapter readChapterFromJson(File file) {
            try (FileReader reader = new FileReader(file)) {
                return GSON.fromJson(reader, BookChapter.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }
}
