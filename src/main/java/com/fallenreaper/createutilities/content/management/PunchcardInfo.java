package com.fallenreaper.createutilities.content.management;

import net.minecraft.nbt.CompoundTag;

public class PunchcardInfo implements ISavedInfo {
    protected CompoundTag data;

    public PunchcardInfo() {
        data = new CompoundTag();
    }

    public CompoundTag getDataInfo() {
        return data;
    }
}
