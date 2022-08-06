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
    public float fuelLevel;
    LazyOptional<IItemHandler> inventoryProvider;
    public TypewriterItemHandler inventory;
    public boolean hasBluePrint;
    public boolean hasFuel;
    int ticks;
    public float dataGatheringProgress;
    boolean shouldSendData;

    public TypewriterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = new TypewriterItemHandler(this);
        inventoryProvider = LazyOptional.of(() -> inventory);
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }
    protected void tryRefill() {
        if (inventory.getStackInSlot(0).isEmpty())
            return;
        //TODO, redo this sometime
        if( 1 - fuelLevel + 1f / 128f < getFuelAddedByEach())
        return;

        inventory.getStackInSlot(0)
                .shrink(1);
        fuelLevel += getFuelAddedByEach();

        sendData();
    }

    @Override
    public void tick() {
        super.tick();

        tryRefill();

        if(fuelLevel <= 0) {
            fuelLevel = 0;
        }

                if(shouldSendData) {
                    dataGatheringProgress += 0.005F;
                }

        if(dataGatheringProgress >= 1f) {
            shouldSendData = false;

            dataGatheringProgress = 0F;
        }


    }
    public void changeFuelLevel() {

        fuelLevel -= getFuelUsageRate();
    }

    public void shouldSend() {
        shouldSendData = true;
    }

    @Override
    public Component getName() {
        return new TextComponent("Typewriter");
    }

    public boolean hasBlueprintIn(){
        if (inventory == null)
            return false;
        return hasBluePrint;
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Typewriter");
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", inventory.serializeNBT());
        compound.putBoolean("ShouldStartProgress", shouldSendData);
        compound.putFloat("FuelLevel", fuelLevel);
        compound.putFloat("DataProgress", dataGatheringProgress);
         compound.putBoolean("HasBlueprint", !this.inventory.getStackInSlot(4).isEmpty());
        compound.putBoolean("HasFuel", !(fuelLevel <= 0));

    }
    public double getFuelUsageRate() {
        return 0.25F;
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
          super.read(compound, clientPacket);
            shouldSendData = compound.getBoolean("ShouldStartProgress");
            hasBluePrint = compound.getBoolean("HasBlueprint");
            fuelLevel = compound.getFloat("FuelLevel");
            hasFuel = compound.getBoolean("HasFuel");
            dataGatheringProgress = compound.getFloat("DataProgress");
            inventory.deserializeNBT(compound.getCompound("Inventory"));
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
    public void onLoad() {
        super.onLoad();
    }

    public double getFuelAddedByEach() {
        return 0.25F;
    }

    @Override
    public void writeSafe(CompoundTag tag, boolean clientPacket) {
        super.writeSafe(tag, clientPacket);
    }
    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (isItemHandlerCap(cap))
            return inventoryProvider.cast();
        return super.getCapability(cap);
    }
}
