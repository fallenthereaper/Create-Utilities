package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.simibubi.create.content.contraptions.fluids.tank.BoilerData;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface ISteamProvider {
    float getProducedSteam();

    BlockPos getBlockPos();

    Level getLevel();

    void notifyUpdate();

    int getTankCapacity();

   BoilerData getBoiler();
   @Nullable
   SmartTileEntity getBlockEntity();
}
