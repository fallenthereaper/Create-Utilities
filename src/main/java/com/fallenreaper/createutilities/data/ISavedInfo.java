package com.fallenreaper.createutilities.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface ISavedInfo {
     String getLabeledText();

      void setLabeledText(String txt);

    static void fromTag(CompoundTag compoundTag) {}

    ResourceLocation getId();

    CompoundTag getData();
    String getTextData(String key);
     default List<Component> getDescription(String type) {
        ResourceLocation id = getId();
        return ImmutableList
                .of(new TranslatableComponent(id.getNamespace() + ".instruction." + type + "." + id.getPath()));
    }
}
