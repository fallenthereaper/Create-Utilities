package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.fallenreaper.createutilities.index.CUItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TypewriterItemHandler extends ItemStackHandler {
    public TypewriterBlockEntity te;

    public TypewriterItemHandler(TypewriterBlockEntity te) {
        super(6);
        this.te = te;
    }

    public void clearContents() {
        for (int i = 0; i < this.getSlots(); i++)
            this.setStackInSlot(i, ItemStack.EMPTY);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return switch (slot) {
            case 4 -> CUItems.NOTE_ITEM.isIn(stack) && !stack.hasTag(); //notes input
            case 5 -> false; //output
            case 0 -> CUItems.WAX.isIn(stack); //fuel input
            case 1 -> CUItems.PUNCHCARD.isIn(stack); // punchcard input
            default -> super.isItemValid(slot, stack);
        };
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        te.notifyUpdate();
    }
}
