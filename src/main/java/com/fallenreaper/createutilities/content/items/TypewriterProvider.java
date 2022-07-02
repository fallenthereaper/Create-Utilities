package com.fallenreaper.createutilities.content.items;

import net.minecraft.nbt.CompoundTag;

public interface TypewriterProvider {
     CompoundTag compoundTag = new CompoundTag();
   default CompoundTag getCompoundTag() {
         return  compoundTag;
     }

}
