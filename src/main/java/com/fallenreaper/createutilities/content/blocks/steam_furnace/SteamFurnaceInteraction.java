package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.fallenreaper.createutilities.core.utils.InteractionHandler;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SteamFurnaceInteraction extends InteractionHandler {
    SmartFluidTank fluidTank;
    @Override
    public void onInteract(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {

    }

    @Override
    public void init() {
        this.fluidTank.setValidator((p->  FluidHelper.isWater(p.getFluid())));
    }

    public SteamFurnaceInteraction(SmartTileEntity te, SmartFluidTank fluidTank) {
        super(te);
        this.fluidTank = fluidTank;

    }


    @Override
    public void tick() {
        super.tick();
    }
}
