package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlockEntity;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerGrowHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FarmBlock.class)
public class FarmlandSurviveMixin {


    /**
     * @author FallenReaper
     * @reason TEST
     */

    @Overwrite(remap = false)
    private static boolean isNearWater(LevelReader pLevel, BlockPos pPos) {
        BlockState lev = pLevel.getBlockState(pPos);
        int radius = 0;
        BlockEntity te = pLevel.getBlockEntity(pPos);
        for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-4, 0, -4), pPos.offset(4, 1, 4))) {
            if (pLevel.getFluidState(blockpos).is(FluidTags.WATER)) {
                return true;
            }
        }

  //shit code i will redo after release, as of now it's just a placeholder
        for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-8, 1, -8), pPos.offset(8, 1, 8))) {
            BlockState leve = pLevel.getBlockState(blockpos);
            BlockEntity ent = pLevel.getBlockEntity(blockpos);
            BlockPos ee = blockpos;
            if (ent instanceof SprinklerBlockEntity e) {
                radius = ((SprinklerBlockEntity) ent).getRadius();
                ee = e.getBlockPos();
            }


            for (BlockPos blockpos1 : BlockPos.betweenClosed(ee.offset(-radius, 1, -radius), ee.offset(radius, 1, radius))) {
                BlockEntity tee = pLevel.getBlockEntity(blockpos1);
                if (tee instanceof SprinklerBlockEntity ref) {
                    if (ref.currentState == SprinklerBlockEntity.State.HYDRATING && ref.isWater()) {
                        if (SprinklerGrowHelper.isInsideCircle(radius, ref.getBlockPos(), blockpos1))
                            return true;
                    }
                }
            }
        }

        return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(pLevel, pPos);

    }
}

