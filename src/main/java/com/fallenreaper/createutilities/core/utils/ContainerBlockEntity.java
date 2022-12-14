package com.fallenreaper.createutilities.core.utils;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
@SuppressWarnings("ALL")
public abstract class ContainerBlockEntity<K extends ItemStackHandler> extends SmartTileEntity {
    public K inventory;
    LazyOptional<K> inventoryProvider;

    public ContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inventoryProvider = LazyOptional.of(() -> inventory);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        inventoryProvider.invalidate();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {

        if (isItemHandlerCap(cap))
            return inventoryProvider.cast();
        return super.getCapability(cap);
    }
}
