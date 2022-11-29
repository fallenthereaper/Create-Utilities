package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SteamFurnaceItemHandler extends ItemStackHandler {
    public SteamFurnaceBlockEntity te;

    public SteamFurnaceItemHandler(SteamFurnaceBlockEntity te, int size) {
        super(size);
        this.te = te;
    }



    @Override
    public void setSize(int size) {
        super.setSize(size);
    }

    @Override
    public int getSlotLimit(int slot) {
        if(te.hasFuel())
            return this.getStackInSlot(0).getMaxStackSize();
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if(slot == 0) {
            return SteamFurnaceBlockEntity.isFuel(stack);
        }
        else return false;

    }
    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        te.notifyUpdate();
    }
}
