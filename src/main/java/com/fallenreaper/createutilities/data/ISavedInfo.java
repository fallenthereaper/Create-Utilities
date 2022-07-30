package com.fallenreaper.createutilities.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public interface ISavedInfo {
     String getLabeledText();

      void setLabeledText(String txt);

    String getId();

    CompoundTag getData();

    String getTextData(String key);

     default List<Component> getDescription(String type) {
        String id = getId();
        return ImmutableList
                .of(new TranslatableComponent(id + ".instruction." + type + "." + id));
    }
}
