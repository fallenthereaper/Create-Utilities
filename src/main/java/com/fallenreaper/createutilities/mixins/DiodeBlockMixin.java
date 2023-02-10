package com.fallenreaper.createutilities.mixins;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import net.minecraft.world.level.block.DiodeBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DiodeBlock.class)
public class DiodeBlockMixin implements IWrenchable {
}
