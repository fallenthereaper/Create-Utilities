package com.fallenreaper.createutilities.networking;

import com.fallenreaper.createutilities.content.blocks.punchcard_writer.PunchcardWriterBlockEntity;
import com.fallenreaper.createutilities.content.blocks.punchcard_writer.PunchcardWriterContainer;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InventoryEditPacket extends SimplePacketBase {

    ItemStackHandler inventory;
    ItemStack itemStack;

    public InventoryEditPacket(ItemStackHandler inv, ItemStack itemStack) {
        this.itemStack = itemStack;
        this.inventory = inv;

    }

    public InventoryEditPacket(FriendlyByteBuf buffer) {
        this.itemStack = buffer.readItem();
        readAdditional(buffer);

    }

    protected void readAdditional(FriendlyByteBuf buffer) {

    }

    protected void writeAdditional(FriendlyByteBuf buffer) {

    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(itemStack);
        writeAdditional(buffer);

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get()
                    .getSender();
            if (player == null)
                return;
            if (!(player.containerMenu instanceof PunchcardWriterContainer container))
                return;
            PunchcardWriterBlockEntity te = ((PunchcardWriterContainer) player.containerMenu).contentHolder;
            ItemStackHandler itemstackHandler = container.contentHolder.inventory;
            itemstackHandler.setStackInSlot(0, itemStack);
            te.notifyUpdate();


        });
        context.get().setPacketHandled(true);
    }
}

