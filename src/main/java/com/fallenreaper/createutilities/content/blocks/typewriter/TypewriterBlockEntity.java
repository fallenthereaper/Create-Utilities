package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
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

public class TypewriterBlockEntity extends SmartTileEntity implements Nameable, MenuProvider {
    public float fuelLevel = 10/4;
    LazyOptional<IItemHandler> inventoryProvider;
    TypewriterItemHandler inventory;

    public TypewriterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = new TypewriterItemHandler(this);
        inventoryProvider = LazyOptional.of(() -> inventory);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }
    protected void refillFuelIfPossible() {

        if (1 - fuelLevel + 1 / 128f < getFuelAddedByGunPowder())
            return;
        if (inventory.getStackInSlot(2)
                .isEmpty())
            return;

        inventory.getStackInSlot(2)
                .shrink(1);
        fuelLevel += getFuelAddedByGunPowder();
        sendData();
    }

    @Override
    public void tick() {
        super.tick();
        refillFuelIfPossible();
        if (fuelLevel <= 0 ) {
            fuelLevel = 0;
        }
    }

    @Override
    public Component getName() {
        return new TextComponent("Typewriter");
    }

    public boolean hasBlueprintIn(){
        return !this.inventory.getStackInSlot(4).isEmpty();
    }


    @Override
    public Component getDisplayName() {
        return new TextComponent("Typewriter");
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("inventory", inventory.serializeNBT());

        super.write(compound, clientPacket);
    }
    public double getFuelUsageRate() {
        return  20 / 100f;
    }
    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        inventory.deserializeNBT(compound.getCompound("inventory"));
    }
    @Override
    public void setRemoved() {
        super.setRemoved();
        inventoryProvider.invalidate();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return TypewriterContainer.create(pContainerId, pInventory, this);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }
    @Override
    public void onLoad() {
        super.onLoad();
    }
    public double getFuelAddedByGunPowder() {
        return 20 / 100f;
    }



    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (isItemHandlerCap(cap))
            return inventoryProvider.cast();
        return super.getCapability(cap);
    }
}
