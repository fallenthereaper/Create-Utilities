package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlock;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlockEntity;
import com.fallenreaper.createutilities.core.data.IFarmBlockAccessor;
import com.fallenreaper.createutilities.core.utils.MiscUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static com.fallenreaper.createutilities.core.utils.MiscUtil.isInsideCircle;

@Mixin(FarmBlock.class)
public abstract class FarmBlockMixin implements IFarmBlockAccessor {

   @Unique
    protected boolean isNearWater;

    @Inject(method = "<init>*", at = @At("TAIL"), remap = false)
    public void init(BlockBehaviour.Properties pProperties, CallbackInfo ci) {
        this.isNearWater = false;
    }

    @Inject(method = "isNearWater", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void detectWater(LevelReader pLevel, BlockPos pPos, CallbackInfoReturnable<Boolean> cir) {

        for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-12, 1, -12), pPos.offset(12, 12, 12))) {
            BlockEntity detectedBlock = pLevel.getBlockEntity(blockpos);

            if (detectedBlock instanceof SprinklerBlockEntity be) {
                BlockPos posBe = be.getBlockPos();
                boolean isCeiling = pLevel.getBlockState(posBe).getValue(SprinklerBlock.CEILING);

                if (be.isHydrating() && be.isWater()) {
                    for (BlockPos pos : BlockPos.betweenClosed(posBe.offset(-be.getRadius(), 1, -be.getRadius()), posBe.offset(be.getRadius(), 12, be.getRadius()))) {
                        if (isInsideCircle(be.getRadius(), posBe, pPos)) {
                            cir.setReturnValue(true);
                        }
                    }
                }
            }
        }
    }
    @Inject(method = "tick", at = @At(value = "HEAD"), remap = false)
     public void onTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRand, CallbackInfo ci) {
        this.isNearWater = MiscUtil.isNearWater(pLevel, pPos);
    }



    @Override
    public boolean getWaterCheck() {
        return this.isNearWater;
    }

    @Override
    public void setWaterCheck(boolean value) {
        this.isNearWater = value;
    }
}

