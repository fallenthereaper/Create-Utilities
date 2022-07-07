package com.fallenreaper.createutilities.content.items;

import net.minecraft.world.item.Item;

public class NoteItem extends BaseItem {
    public NoteItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getMaxClicks() {
        return 32;
    }
}
