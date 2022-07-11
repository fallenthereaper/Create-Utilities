package com.fallenreaper.createutilities.content.items;

public class PunchcardItem extends BaseItem   {
    public PunchcardItem(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public int getMaxClicks() {
        return super.getMaxClicks()/4;
    }
}
