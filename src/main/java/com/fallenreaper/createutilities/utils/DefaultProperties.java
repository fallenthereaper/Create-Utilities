package com.fallenreaper.createutilities.utils;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.SharedProperties;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

@MethodsReturnNonnullByDefault
public class DefaultProperties extends SharedProperties {

    public static Block brassMetal() {
        return AllBlocks.BRASS_BLOCK.get();
    }

    public static Block crystal() {
        return Blocks.AMETHYST_BLOCK;
    }

}
