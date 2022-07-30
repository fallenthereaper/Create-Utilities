package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowBlockEntity;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowDisplaySource;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowInstance;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowRenderer;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoorBlockEntity;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlockEntity;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerInstance;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerRenderer;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterBlockEntity;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterRenderer;
import com.simibubi.create.content.curiosities.deco.SlidingDoorRenderer;
import com.simibubi.create.repack.registrate.util.entry.BlockEntityEntry;

import static com.simibubi.create.content.logistics.block.display.AllDisplayBehaviours.assignDataBehaviourTE;

public class CUBlockEntities {
    public static final BlockEntityEntry<SprinklerBlockEntity> SPRINKLER = CreateUtilities.registrate()
            .tileEntity("sprinkler", SprinklerBlockEntity::new)
            .instance(() -> SprinklerInstance::new)
            .validBlock(CUBlocks.SPRINKLER)
            .renderer(() -> SprinklerRenderer::new)
            .register();
    public static final BlockEntityEntry<TypewriterBlockEntity> TYPEWRITER = CreateUtilities.registrate()
            .tileEntity("typewriter", TypewriterBlockEntity::new)
            .validBlock(CUBlocks.TYPEWRITER)
            .renderer(() -> TypewriterRenderer::new)
            .register();
    public static final BlockEntityEntry<BellowBlockEntity> BELLOW = CreateUtilities.registrate()
            .tileEntity("bellow", BellowBlockEntity::new)
            .instance(() -> BellowInstance::new)
            .validBlock(CUBlocks.BELLOWS)
            .renderer(() -> BellowRenderer::new)
            .onRegister(assignDataBehaviourTE(new BellowDisplaySource()))
            .register();
    public static final BlockEntityEntry<LockSlidingDoorBlockEntity> SLIDING_DOOR = CreateUtilities.registrate()
            .tileEntity("brass_sliding_door", LockSlidingDoorBlockEntity::new)
            .renderer(() -> SlidingDoorRenderer::new)
            .validBlocks(CUBlocks.BRASS_DOOR)
            .register();

    public static void register() {
    }
}
