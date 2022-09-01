package com.fallenreaper.createutilities.grouptabs;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;


public abstract class ModItemTab extends CreativeModeTab {


    //static ItemTabProvider<ItemStack> providerMethodRef = CUBlocks.SPRINKLER::asStack;
    public TextComponent text;
    public ItemStack icon;
    // public ItemTabProvider<ItemStack> provider = () -> icon;

    public ModItemTab(String label) {
        super(label);
    }

    public ModItemTab(String label, ItemStack sup, TextComponent text) {
        super(label);
        this.icon = sup;
        this.text = text;
    }


    @Override
    public int getSlotColor() {
        return super.getSlotColor() - 1;
    }

    @Override
    public boolean showTitle() {
        return super.showTitle();
    }

}
