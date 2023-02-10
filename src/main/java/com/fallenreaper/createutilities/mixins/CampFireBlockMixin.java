package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.core.data.ISmokeSource;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(CampfireBlock.class)
public abstract class CampFireBlockMixin {

        @Inject(at = @At("RETURN"), method = "isSmokeSource(Lnet/minecraft/world/level/block/state/BlockState;)Z", cancellable = true)
        public void createutilities_isSmokeSource(BlockState state, CallbackInfoReturnable<Boolean> cir) {
            if (state.getBlock() instanceof ISmokeSource) {
                cir.setReturnValue(true);
            }
    }
}
