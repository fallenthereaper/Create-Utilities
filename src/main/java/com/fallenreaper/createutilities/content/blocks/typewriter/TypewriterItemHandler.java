package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.fallenreaper.createutilities.index.CUItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TypewriterItemHandler extends ItemStackHandler {
    public TypewriterBlockEntity te;

    public  TypewriterItemHandler(TypewriterBlockEntity te) {
        super(10);
        this.te = te;

    }

    @Override
    public int getSlotLimit(int slot) {
        if(slot == 2) {
            return super.getSlotLimit(slot);
        }
        return 1;

    }


    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return switch (slot) {
            case 4 -> CUItems.NOTE_ITEM.isIn(stack);
            case 5 -> false;
            case 0 -> CUItems.WAX.isIn(stack);
            default -> super.isItemValid(slot, stack);
        };
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        te.notifyUpdate();
    }
}
