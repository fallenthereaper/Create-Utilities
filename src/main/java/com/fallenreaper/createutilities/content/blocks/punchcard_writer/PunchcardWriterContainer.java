package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.fallenreaper.createutilities.index.CUContainerTypes;
import com.simibubi.create.foundation.gui.container.ContainerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PunchcardWriterContainer extends ContainerBase<PunchcardWriterBlockEntity> {
    ContainerLevelAccess access;

    public PunchcardWriterContainer(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
        //  addAccess();
    }

    protected PunchcardWriterContainer(MenuType<?> type, int id, Inventory inv, PunchcardWriterBlockEntity contentHolder, ContainerLevelAccess access) {
        super(type, id, inv, contentHolder);
        addAccess();
    }

    public static PunchcardWriterContainer create(int id, Inventory inv, PunchcardWriterBlockEntity te, ContainerLevelAccess access) {

        return new PunchcardWriterContainer(CUContainerTypes.PUNCHCARD_WRITER.get(), id, inv, te, access);
    }

    void addAccess() {
        this.access = contentHolder.levelAccess;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot clickedSlot = getSlot(pIndex);
        if (!clickedSlot.hasItem())
            return ItemStack.EMPTY;
        ItemStack stack = clickedSlot.getItem();

        if (pIndex < slots.size()) {
            moveItemStackTo(stack, 0, slots.size(), false);
        } else {
            if (moveItemStackTo(stack, 0, 1, false) || moveItemStackTo(stack, 2, 3, false)
                    || moveItemStackTo(stack, 4, 5, false)) ;
        }

        return ItemStack.EMPTY;
    }

    @Override
    protected PunchcardWriterBlockEntity createOnClient(FriendlyByteBuf extraData) {
        ClientLevel world = Minecraft.getInstance().level;
        BlockEntity tileEntity = world.getBlockEntity(extraData.readBlockPos());
        if (tileEntity instanceof PunchcardWriterBlockEntity typewriter) {
            typewriter.readClient(extraData.readNbt());
            return typewriter;
        }
        return null;
    }

    @Override
    protected void initAndReadInventory(PunchcardWriterBlockEntity contentHolder) {

    }

    @Override
    protected void addSlots() {

        addSlot(new PunchcardWriterSlot(contentHolder.inventory, contentHolder, 0, 53 + 30, 33, this.access, player));

        //    for (int row = 0; row < 2; ++row) {
        //  for (int col = 0; col < 9; ++col)
        //    this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 18 * col + 64 - 10, (row * 18) + 199));
        //     }
        // addPlayerSlots(8, 148);

    }

    @Override
    protected void addPlayerSlots(int x, int y) {
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 9; ++col)
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 18 * col + x, (row * 18) + y));
        }
    }

    @Override
    protected void saveData(PunchcardWriterBlockEntity contentHolder) {

    }


}
