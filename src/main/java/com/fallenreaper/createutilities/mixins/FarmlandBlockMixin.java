package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlockEntity;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerInteractionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FarmBlock.class)
public class FarmlandBlockMixin {


    /**
     * @author FallenReaper
     * @reason Overwriting
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
                int radius = be.getRadius();

                    for (BlockPos pos : BlockPos.betweenClosed(blockpos.offset(-radius, -1, -radius), blockpos.offset(radius, -1, radius))) {
                        if (be.isHydrating() && be.isWater()) {
                        if (SprinklerInteractionHandler.isInsideCircle(radius, blockpos, pos)) {
                            return true;
                        }
                    }
                }
            }
        }

        return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(pLevel, pPos);
    }
}

