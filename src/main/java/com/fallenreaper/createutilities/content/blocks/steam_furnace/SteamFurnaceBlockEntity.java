package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.fallenreaper.createutilities.utils.data.IDevInfo;
import com.fallenreaper.createutilities.utils.data.blocks.InteractableBlockEntity;
import com.simibubi.create.content.contraptions.fluids.tank.BoilerData;
import com.simibubi.create.content.contraptions.fluids.tank.FluidTankTileEntity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SteamFurnaceBlockEntity extends InteractableBlockEntity implements IHaveGoggleInformation, ISteamProvider, IDevInfo {
    public BoilerData boiler;
    public SteamFurnaceBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public InteractionResult onInteract(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        return super.onInteract(pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        tooltip.add(new TranslatableComponent("inventory.hotbarInfo"));
        return true;
    }

    @Override
    public float getProducedSteam() {
        return 0;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("BoilerData", boiler.write());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        boiler.read(compound.getCompound("BoilerData"), 2);
    }
    public void updateBoilerState() {


        boolean wasBoiler = boiler.isActive();
        boolean changed = boiler.evaluate(this);

        if (wasBoiler != boiler.isActive()) {
            if (boiler.isActive())
                setWindows(false);

            for (int yOffset = 0; yOffset < height; yOffset++)
                for (int xOffset = 0; xOffset < width; xOffset++)
                    for (int zOffset = 0; zOffset < width; zOffset++)
                        if (level.getBlockEntity(
                                worldPosition.offset(xOffset, yOffset, zOffset))instanceof FluidTankTileEntity fte)
                            fte.refreshCapability();
        }

        if (changed) {
            notifyUpdate();
            boiler.checkPipeOrganAdvancement(this);
        }
    }

    @Override
    public String getProvidedInfo() {
        return null;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return super.getCapability(cap);
    }
}
