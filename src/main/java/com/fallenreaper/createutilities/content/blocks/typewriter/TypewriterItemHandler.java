package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.curiosities.toolbox.ToolboxTileEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class TypewriterItemHandler extends ItemStackHandler {
    private TypewriterBlockEntity te;

    public TypewriterItemHandler() {
        super(1);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem().equals(AllItems.CRAFTING_BLUEPRINT);
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        te.setChanged();
        te.sendData();
    }
}
