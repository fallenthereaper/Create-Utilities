package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.core.data.ISmokeSource;
import net.minecraft.world.level.block.HayBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HayBlock.class)
public class HaybaleBlockMixin implements ISmokeSource {

}
