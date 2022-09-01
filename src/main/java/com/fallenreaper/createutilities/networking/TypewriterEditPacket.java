package com.fallenreaper.createutilities.networking;

import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterBlockEntity;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterContainer;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TypewriterEditPacket extends SimplePacketBase {
    ItemStackHandler inventory;
    ItemStack itemStack;
    CompoundTag tag;
    boolean should;

    public TypewriterEditPacket(ItemStackHandler inv, ItemStack itemStack, CompoundTag tag, boolean should) {
        this.itemStack = itemStack;
        this.inventory = inv;
        this.tag = tag;
        this.should = should;

    }

    public TypewriterEditPacket(FriendlyByteBuf buffer) {

        this.itemStack = buffer.readItem();
        this.tag = buffer.readNbt();
        this.should = buffer.readBoolean();
        readAdditional(buffer);

    }

    protected void readAdditional(FriendlyByteBuf buffer) {

    }

    protected void writeAdditional(FriendlyByteBuf buffer) {

    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(itemStack);
        buffer.writeNbt(tag);
        buffer.writeBoolean(should);
        writeAdditional(buffer);

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get()
                    .getSender();
            if (player == null)
                return;
            if (!(player.containerMenu instanceof TypewriterContainer container))
                return;
            TypewriterBlockEntity te = ((TypewriterContainer) player.containerMenu).contentHolder;
            ItemStackHandler itemstackHandler = container.contentHolder.inventory;
            System.out.println(itemStack.getItem().getDescriptionId() + 2);

            te.changeFuelLevel();
            te.notifyUpdate();
            if (should) {
                te.shouldSend();
            }

            if (te.dataGatheringProgress >= 0.9F) {
                itemstackHandler.setStackInSlot(4, ItemStack.EMPTY);
                System.out.println(itemStack.getItem().getDescriptionId() + 3);
                itemstackHandler.setStackInSlot(5, itemStack);
                te.notifyUpdate();
            }

        });
        context.get().setPacketHandled(true);
    }
}
