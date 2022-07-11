package com.fallenreaper.createutilities.content.items;



public class NotesItem extends BaseItem {
    public NotesItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxClicks() {
        return super.getMaxClicks()/2;
    }
}
