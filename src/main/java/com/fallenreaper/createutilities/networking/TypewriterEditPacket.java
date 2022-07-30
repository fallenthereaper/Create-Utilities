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

    public TypewriterEditPacket(ItemStackHandler inv, ItemStack itemStack, CompoundTag tag) {
        this.itemStack = itemStack;
        this.inventory = inv;
        this.tag = tag;

    }

    public TypewriterEditPacket(FriendlyByteBuf buffer) {

      this.itemStack = buffer.readItem();
        this.tag = buffer.readNbt();
        readAdditional(buffer);

    }
    protected void readAdditional(FriendlyByteBuf buffer) {

    };

    protected void writeAdditional(FriendlyByteBuf buffer) {

    };
    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(itemStack);
        buffer.writeNbt(tag);
        writeAdditional(buffer);

    }
    @Override
    public void handle(Supplier<NetworkEvent.Context> context) {
      context.get().enqueueWork(()->{
          ServerPlayer player = context.get()
                  .getSender();
          if (player == null)
              return;
          if (!(player.containerMenu instanceof TypewriterContainer container))
              return;
          System.out.println(itemStack.getItem().getDescriptionId() + 1);
                      ItemStackHandler itemstackHandler = container.contentHolder.inventory;
          System.out.println(itemStack.getItem().getDescriptionId() + 2);
          itemstackHandler.setStackInSlot(4, ItemStack.EMPTY);

          TypewriterBlockEntity te = ((TypewriterContainer) player.containerMenu).contentHolder;

    te.changeFuelLevel();
    te.shouldSend();
    te.sendData();

                           System.out.println(itemStack.getItem().getDescriptionId() + 3);
                          itemstackHandler.setStackInSlot(5, itemStack);


      });
        context.get().setPacketHandled(true);
    }
}
