package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.index.CUItems;
import com.fallenreaper.createutilities.core.utils.ContainerBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class PunchcardWriterItemHandler extends ItemStackHandler {
    public ContainerBlockEntity<PunchcardWriterItemHandler> be;

    public PunchcardWriterItemHandler(ContainerBlockEntity<PunchcardWriterItemHandler> be) {
        super(2);
        this.be = be;

    }

    public PunchcardWriterItemHandler(int size) {
        super(size);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch (slot) {
            case 0 -> CUItems.PUNCHCARD.isIn(stack);
            case 1 -> false;
            default -> super.isItemValid(slot, stack);
        };
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        be.notifyUpdate();
    }

    @Override
    protected void onLoad() {

    }
}
