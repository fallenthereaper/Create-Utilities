package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PunchcardWriterBlockEntity extends SmartTileEntity implements MenuProvider {
    LazyOptional<IItemHandler> inventoryProvider;
    public PunchcardWriterItemHandler inventory;
    public boolean hasPunchcard;
    public PunchcardWriterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = new PunchcardWriterItemHandler(this);
        inventoryProvider = LazyOptional.of(() -> inventory);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }
    @Override
    public void setRemoved() {
        super.setRemoved();
        inventoryProvider.invalidate();
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", inventory.serializeNBT());
        compound.putBoolean("HasPunchcard", !inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(0).hasTag());


    }
    public boolean hasPunchcard() {
        return hasPunchcard;
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        hasPunchcard = compound.getBoolean("HasPunchcard");
    }
    @Override
    public Component getDisplayName() {
        return new TextComponent("Punchcard Writer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return PunchcardWriterContainer.create(pContainerId, pPlayerInventory, this);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (isItemHandlerCap(cap))
            return inventoryProvider.cast();
        return super.getCapability(cap);
    }
}
