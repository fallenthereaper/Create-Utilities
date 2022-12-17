package com.fallenreaper.createutilities.core.data;

import com.simibubi.create.content.contraptions.fluids.tank.BoilerData;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Implement this interface if you want to add a custom boiler used on steam engines.
 */
public interface IBoilerProvider<T extends SmartTileEntity, B extends BoilerData> {
    float getProducedSteam();

    Level getLevel();

    void notifyUpdate();

    int getTankCapacity();

 @NotNull B getBoiler();

   void updateBoiler();

    static void tryUpdateBoiler(Level pLevel, BlockPos tankPos) {
        BlockEntity be = pLevel.getBlockEntity(tankPos);
        if(be == null)
            return;
        if (!(be instanceof IBoilerProvider<?, ?> te))
            return;

        te.updateBoiler();
    }

   @Nullable
   T getBlockEntity();
}
