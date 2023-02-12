package com.fallenreaper.createutilities.core.data.gui;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.core.data.gui.file_management.BookContentBuilder;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.fallenreaper.createutilities.core.utils.MiscUtil.deserializeJson;
import static com.fallenreaper.createutilities.core.utils.MiscUtil.serializeJson;

@SuppressWarnings("all")
public class BookChapter {
  protected List<BookPage> bookPages;
  protected Component chapterTitle;


    public BookChapter(Component chapterTitle, BookPage... bookPages) {
        this.bookPages = Arrays.asList(bookPages);
        this.chapterTitle = chapterTitle;
    }

  public BookPage getBookPage(int index) {
        try {
            return bookPages.get(index);
        }
        catch (Exception exception) {
            CreateUtilities.LOGGER.warn("Invalid index, book page not found");
            return null;
        }
    }

    public Component getChapterTitle() {
        return chapterTitle;
    }

    public List<BookPage> getBookPages() {
        return bookPages;
    }

    public BookChapter addBookPage(BookPage bookPage) {

         if(bookPage != null) bookPages.add(bookPage);
         return this;
    }


    public static void writeChapterToJson(String filename, BookChapter... chapter) {
        try (FileWriter writer = new FileWriter(filename)){
            serializeJson(BookChapter[].class, writer, BookContentBuilder.GSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //todo, use Resource location
    public static BookChapter readChapterFromJson(String filename) {
        try(FileReader reader = new FileReader(filename);) {
            BookChapter chapter = deserializeJson(BookChapter.class, reader, BookContentBuilder.GSON);
            reader.close();
            return chapter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static BookChapter generatePage(String res) {
       // Optional<Resource> resource;
        Optional<File> file;
        BookChapter page = null;
        try {
           // resource = Minecraft.getInstance().getResourceManager().getResource(res);
            try {
              //  resource = Minecraft.getInstance().getResourceManager().getResource(res);
                file = Optional.of(new File(res));
             //   if (resource.isPresent()) {
                if(file.isPresent()) {
                    BufferedReader s = new BufferedReader(new FileReader(file.get()));

                    ;
                //    BufferedReader inputstream = resource.get().openAsReader();
                //    page = deserialize(inputstream);
                }
             //   }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            return null;
        }
        return page;
    }


}
