package com.fallenreaper.createutilities.core.data.punchcard;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.core.data.ISavedInfo;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public abstract class PunchcardInfo implements ISavedInfo {

    public CompoundTag data;
    protected String description;


    public PunchcardInfo() {
        data = new CompoundTag();
        LangBuilder lang = Lang.builder(CreateUtilities.ID);
        Component txt = new TextComponent(lang.translate("instruction." + getId()).string());

        data.putString("text", txt.getString());

    }

    public CompoundTag getDataInfo() {
        return data;
    }

    @Override
    public String getTextData(String key) {
        if (!data.contains(key))
            return " ";

        return data.getString(key);
    }

    public CompoundTag getTagInfo() {
        //info for this class and all other PunchcardInfos
        CompoundTag tag = new CompoundTag();
        tag.putString("Id", getLabeledText());
        tag.put("Data", this.data.copy());

        LangBuilder lang = Lang.builder(CreateUtilities.ID);
        Component txt = new TextComponent(lang.translate("instruction." + getId()).string());
        tag.getCompound("Data").putString("text", txt.getString());


        return tag;
    }


    @Override
    public String getLabeledText() {
        return getTextData("text");
    }

    @Override
    public void setLabeledText(String txt) {

    }

    @Override
    public String getId() {
        return CreateUtilities.defaultResourceLocation("info").getPath();
    }

    @Override
    public CompoundTag getData() {
        return data;
    }


}
