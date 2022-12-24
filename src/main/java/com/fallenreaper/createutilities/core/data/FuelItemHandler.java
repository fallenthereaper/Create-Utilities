package com.fallenreaper.createutilities.core.data;

import com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import static com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlockEntity.isFuel;

public class FuelItemHandler extends ItemStackHandler {
    public SteamFurnaceBlockEntity te;

    public FuelItemHandler(SteamFurnaceBlockEntity te, int size) {
        super(size);
        this.te = te;
    }

    public void setNewSlot(NonNullList<ItemStack> copy) {
        this.stacks = copy;
    }
    public NonNullList<ItemStack> getAllSlots() {
        return this.stacks;
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
            return isFuel(stack);
        }
        else return false;

    }
    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        te.notifyUpdate();
    }
}
