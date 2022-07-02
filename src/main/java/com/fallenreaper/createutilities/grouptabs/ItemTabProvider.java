package com.fallenreaper.createutilities.grouptabs;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ItemTabProvider<T extends ItemStack> {

    T createIcon();
}
