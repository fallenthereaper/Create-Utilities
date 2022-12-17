package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowBlockEntity;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowDisplaySource;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowInstance;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowRenderer;
import com.fallenreaper.createutilities.content.blocks.mechanical_propeller.MechanicalPropellerBlockEntity;
import com.fallenreaper.createutilities.content.blocks.mechanical_propeller.MechanicalPropellerRenderer;
import com.fallenreaper.createutilities.content.blocks.punchcard_writer.PunchcardWriterBlockEntity;
import com.fallenreaper.createutilities.content.blocks.punchcard_writer.PunchcardWriterRenderer;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoorBlockEntity;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlockEntity;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerInstance;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerRenderer;
import com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlockEntity;
import com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceRenderer;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterBlockEntity;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterRenderer;
import com.fallenreaper.createutilities.core.data.blocks.liquidtank.LiquidTankBlockEntity;
import com.fallenreaper.createutilities.core.data.blocks.liquidtank.LiquidTankRenderer;
import com.simibubi.create.Create;
import com.simibubi.create.content.curiosities.deco.SlidingDoorRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

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
    public static final BlockEntityEntry<MechanicalPropellerBlockEntity> MECHANICAL_PROPELLER = Create.REGISTRATE
            .tileEntity("mechanical_propeller", MechanicalPropellerBlockEntity::new)
            .validBlocks(CUBlocks.MECHANICAL_PROPELLER)
            .renderer(() -> MechanicalPropellerRenderer::new)
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
    public static final BlockEntityEntry<PunchcardWriterBlockEntity> PUNCHCARD_WRITER = CreateUtilities.registrate()
            .tileEntity("punchcard_writer", PunchcardWriterBlockEntity::new)
            .validBlock(CUBlocks.PUNCHCARD_WRITER)
            .renderer(() -> PunchcardWriterRenderer::new)
            .register();

    public static final BlockEntityEntry<LiquidTankBlockEntity> LIQUID_TANK = CreateUtilities.registrate()
            .tileEntity("liquid_tank", LiquidTankBlockEntity::new)
            .validBlock(CUBlocks.LIQUID_TANK)
            .renderer(() -> LiquidTankRenderer::new)
            .register();

    public static final BlockEntityEntry<SteamFurnaceBlockEntity> BOILER_FURNACE = CreateUtilities.registrate()
            .tileEntity("steam_furnace", SteamFurnaceBlockEntity::new)
            .validBlocks(CUBlocks.BOILER_FURNACE)
            .renderer(() -> SteamFurnaceRenderer::new)
            .register();

    public static void register() {
    }
}
