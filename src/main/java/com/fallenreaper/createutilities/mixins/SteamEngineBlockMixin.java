package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.core.data.IBoilerProvider;
import com.simibubi.create.content.contraptions.components.steam.SteamEngineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.fallenreaper.createutilities.core.data.IBoilerProvider.tryUpdateBoiler;
import static com.simibubi.create.content.contraptions.components.steam.SteamEngineBlock.getFacing;

@Mixin(SteamEngineBlock.class)
public class SteamEngineBlockMixin {

    @Inject(method = "canAttach", at = @At(value = "RETURN"), remap = false, cancellable = true)
    private static void onAttach(LevelReader pReader, BlockPos pPos, Direction pDirection, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockpos = pPos.relative(pDirection);
        BlockEntity blockEntity = pReader.getBlockEntity(blockpos);
        if(blockEntity instanceof IBoilerProvider<?, ?>)
           cir.setReturnValue(true);
    }

    @Inject(method = "onPlace", at = @At(value = "HEAD"), remap = false)
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving, CallbackInfo ci) {
        Block block = pLevel.getBlockState(pPos.relative(getFacing(pState).getOpposite())).getBlock();
        BlockEntity be = pLevel.getBlockEntity(pPos.relative(getFacing(pState).getOpposite()));
        if(be instanceof IBoilerProvider<?, ?>)
           tryUpdateBoiler(pLevel, pPos.relative(getFacing(pState).getOpposite()));

        //   SteamFurnaceBlock.updateBoilerState(pState, pLevel, pPos.relative(getFacing(pState).getOpposite()));

    }

    /**
     * @author FallenReaper
     * @reason r
     */
    @Inject(method = "onRemove", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/fluids/tank/FluidTankBlock;updateBoilerState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V", shift = At.Shift.AFTER), remap = false)
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving, CallbackInfo ci) {
        Block block = pLevel.getBlockState(pPos.relative(getFacing(pState).getOpposite())).getBlock();
        BlockEntity be = pLevel.getBlockEntity(pPos.relative(getFacing(pState).getOpposite()));
        if (be instanceof IBoilerProvider<?, ?>)
          tryUpdateBoiler(pLevel, pPos.relative(getFacing(pState).getOpposite()));
         //   SteamFurnaceBlock.updateBoilerState(pState, pLevel, pPos.relative(getFacing(pState).getOpposite()));

    }
}
