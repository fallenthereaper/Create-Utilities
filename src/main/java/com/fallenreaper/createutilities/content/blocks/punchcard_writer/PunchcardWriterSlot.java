package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.content.items.PunchcardItem;
import com.fallenreaper.createutilities.networking.ModPackets;
import com.fallenreaper.createutilities.networking.InventoryEditPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

@SuppressWarnings("ALL")
public class PunchcardWriterSlot extends SlotItemHandler {
    public ContainerLevelAccess access;
    public Player player;
    private PunchcardWriterBlockEntity be;

    public PunchcardWriterSlot(IItemHandler itemHandler, PunchcardWriterBlockEntity be, int pIndex, int pX, int pY, ContainerLevelAccess access, Player minecraft) {
        super(itemHandler, pIndex, pX, pY);
        this.access = access;
        this.be = be;
        this.player = minecraft;
    }

    public ContainerLevelAccess getAccess() {
        return access;
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        super.onTake(pPlayer, pStack);
        if (pPlayer != null) {
            if (getItemHandler() instanceof ItemStackHandler handler) {

                pPlayer.getInventory().placeItemBackInInventory(pStack);
                handler.setStackInSlot(getContainerSlot(), ItemStack.EMPTY);
                ModPackets.channel.sendToServer(new InventoryEditPacket(handler, ItemStack.EMPTY));

                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                pPlayer.closeContainer();
            }
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (getItemHandler().getStackInSlot(getContainerSlot()).getItem() instanceof PunchcardItem item) {
            item.onSlotChanged();
        }

    }

}
