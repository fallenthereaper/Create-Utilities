package com.fallenreaper.createutilities.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class PunchcardInfo implements ISavedInfo {

    protected CompoundTag data;
    protected String description;

    public PunchcardInfo(String description) {
        data = new CompoundTag();
        this.description = description;
       data.putString("text", description);
    }

    public CompoundTag getDataInfo() {
        return data;
    }

    @Override
    public String getTextData(String key) {
        if(!data.contains(key))
            return " ";

        return data.getString(key);
    }

    @Override
    public String getLabeledText() {
        return getTextData("text");
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        return tag;
    }

    public static PunchcardInfo fromTag(CompoundTag tag) {

        return null;
    }

    @Override
    public void setLabeledText(String text) {
       this.description = text;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public CompoundTag getData() {
        return null;
    }

}
