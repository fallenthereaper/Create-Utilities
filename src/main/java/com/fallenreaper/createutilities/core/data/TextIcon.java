package com.fallenreaper.createutilities.core.data;


import com.fallenreaper.createutilities.core.events.DataEvents;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class TextIcon<F extends String, E extends String> {
    private F fillIcon;
    private E emptyIcon;
    private Map<String, Integer> size;

    private TextIcon(F fillIcon, E emptyIcon) {
        this.size = new HashMap<>();
        this.size.put(fillIcon, fillIcon.length());
        this.size.put(emptyIcon, emptyIcon.length());
        this.fillIcon = fillIcon;
        this.emptyIcon = emptyIcon;
        DataEvents.writerTranslations.add(emptyIcon);
        DataEvents.writerTranslations.add(fillIcon);
    }

    public static TextIcon<String, String> of(String icon1, String icon2) {
        return new TextIcon(icon1, icon2);
    }

    public int getSize(String type) {
        return size.get(type);
    }

    public String getFullIcon() {
        return fillIcon;
    }

    public String getEmptyIcon() {
        return emptyIcon;
    }

    public TextIcon swap() {
        this.emptyIcon = (E) fillIcon;
        this.fillIcon = (F) emptyIcon;
        return this;
    }

    TextIcon setIcon(F icon1, E icon2) {
        this.fillIcon = icon1;
        this.emptyIcon = icon2;
        return this;
    }
}
