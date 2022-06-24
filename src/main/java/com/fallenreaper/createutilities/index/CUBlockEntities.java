package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.blocks.sprinkler.SprinklerInstance;
import com.fallenreaper.createutilities.blocks.sprinkler.SprinklerRenderer;
import com.fallenreaper.createutilities.blocks.sprinkler.SprinklerBlockEntity;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

public class CUBlockEntities {
    public static final BlockEntityEntry<SprinklerBlockEntity> SPRINKLER = CreateUtilities.registrate()
            .tileEntity("sprinkler", SprinklerBlockEntity::new)
            .instance(() -> SprinklerInstance::new, false)
            .validBlocks(CUBlocks.SPRINKLER)
            .renderer(() -> SprinklerRenderer::new)

            .register();
    public static void register() {}
}
