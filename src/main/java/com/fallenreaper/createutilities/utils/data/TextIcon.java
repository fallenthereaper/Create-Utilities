package com.fallenreaper.createutilities.utils.data;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class TextIcon {
    private String fillIcon;
    private String emptyIcon;
    private Map<String, Integer> size;

    private TextIcon(String fillIcon, String emptyIcon) {
        this.size = new HashMap<>();
        this.size.put(fillIcon, fillIcon.length());
        this.size.put(emptyIcon, emptyIcon.length());
        this.fillIcon = fillIcon;
        this.emptyIcon = emptyIcon;
    }

    public static TextIcon create(String icon1, String icon2) {
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
        this.emptyIcon = fillIcon;
        this.fillIcon = emptyIcon;
        return this;
    }

    public TextIcon setIcon(String icon1, String icon2) {
        this.fillIcon = icon1;
        this.emptyIcon = icon2;
        return this;
    }
}
