package com.fallenreaper.createutilities.core.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import java.util.List;

import static com.fallenreaper.createutilities.CreateUtilities.ID;

public interface ISavedInfo {
    String getLabeledText();

    void setLabeledText(String txt);

    String getId();

    CompoundTag getData();

    String getTextData(String key);

    default List<Component> getDescription(String type) {
        String id = getId();
        return ImmutableList
                .of( Component.translatable(ID + ".instruction." + type + "." + id));
    }
}
