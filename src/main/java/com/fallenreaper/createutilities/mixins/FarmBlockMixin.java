package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlockEntity;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerInteractionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FarmBlock.class)
public class FarmBlockMixin extends Block {

    public FarmBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    /**
     * @author FallenReaper
     * @reason Null
     */
    @Overwrite(remap = false)
    private static boolean isNearWater(LevelReader pLevel, BlockPos pPos) {
        for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-4, 0, -4), pPos.offset(4, 1, 4))) {
            if (pLevel.getFluidState(blockpos).is(FluidTags.WATER)) {
                return true;
            }
        }

  //messy code I will redo after release, as of now it's just a placeholder
        for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-8, 1, -8), pPos.offset(8, 1, 8))) {
            BlockEntity detectedBlock = pLevel.getBlockEntity(blockpos);

            if (detectedBlock instanceof SprinklerBlockEntity be) {
                BlockPos posBe = be.getBlockPos();
                if(be.isHydrating() && be.isWater()) {
                    for (BlockPos pos : BlockPos.betweenClosed(posBe.offset(-be.getRadius(), 1, -be.getRadius()), posBe.offset(be.getRadius(), 1, be.getRadius()))) {
                        if(SprinklerInteractionHandler.isInsideCircle(be.getRadius(), posBe, pPos)) {
                            return true;
                        }
                    }
                }
            }
        }

        return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(pLevel, pPos);
    }
}

