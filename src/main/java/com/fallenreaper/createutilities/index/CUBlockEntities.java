package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowBlockEntity;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowRenderer;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlockEntity;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerInstance;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerRenderer;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterBlockEntity;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterRenderer;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

public class CUBlockEntities {
    public static final BlockEntityEntry<SprinklerBlockEntity> SPRINKLER = CreateUtilities.registrate()
            .tileEntity("sprinkler", SprinklerBlockEntity::new)
            .instance(() -> SprinklerInstance::new, false)
            .validBlocks(CUBlocks.SPRINKLER)
            .renderer(() -> SprinklerRenderer::new)
            .register();
    public static final BlockEntityEntry<TypewriterBlockEntity> TYPEWRITER = CreateUtilities.registrate()
            .tileEntity("typewriter", TypewriterBlockEntity::new)
            .validBlocks(CUBlocks.TYPEWRITER)
            .renderer(() -> TypewriterRenderer::new)
            .register();
    public static final BlockEntityEntry<BellowBlockEntity> BELLOW = CreateUtilities.registrate()
            .tileEntity("bellow", BellowBlockEntity::new)
            .validBlocks(CUBlocks.BELLOWS)
            .renderer(() -> BellowRenderer::new)
            .register();

    public static void register() {
    }
}
