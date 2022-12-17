package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowBlock;
import com.fallenreaper.createutilities.content.blocks.mechanical_propeller.MechanicalPropellerBlock;
import com.fallenreaper.createutilities.content.blocks.punchcard_writer.PunchcardWriterBlock;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoor;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlock;
import com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlock;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterBlock;
import com.fallenreaper.createutilities.core.utils.DefaultProperties;
import com.fallenreaper.createutilities.core.data.blocks.liquidtank.LiquidTankBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.simibubi.create.repack.registrate.util.nullness.NonNullFunction;
import com.simibubi.create.repack.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.Shapes;

import static com.simibubi.create.AllTags.axeOrPickaxe;
import static com.simibubi.create.AllTags.pickaxeOnly;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class CUBlocks {
    private static final CreateRegistrate REGISTRATE = CreateUtilities.registrate().creativeModeTab(() -> CreateUtilities.TAB);

    public static final BlockEntry<SprinklerBlock> SPRINKLER = REGISTRATE.block("sprinkler", SprinklerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .addLayer(() -> RenderType::cutoutMipped)
            .defaultLoot()
            .register();
    public static final BlockEntry<LockSlidingDoor> BRASS_DOOR = REGISTRATE.block("brass_door", LockSlidingDoor::new)
            .transform(BuilderTransformers.slidingDoor("brass"))
            .properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion())
            .register();
    public static final BlockEntry<TypewriterBlock> TYPEWRITER = REGISTRATE.block("typewriter", TypewriterBlock::new)
            .initialProperties(DefaultProperties::brassMetal)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .transform(customItemModel())
            .addLayer(() -> RenderType::cutoutMipped)
            .defaultLoot()
            .register();
    public static final BlockEntry<MechanicalPropellerBlock> MECHANICAL_PROPELLER = REGISTRATE.block("mechanical_propeller", MechanicalPropellerBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.PODZOL))
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setImpact(4.0D))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<BellowBlock> BELLOWS = REGISTRATE.block("bellow", BellowBlock::new)
            .initialProperties(DefaultProperties::brassMetal)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .transform(customItemModel())
            .addLayer(() -> RenderType::cutoutMipped)
            .defaultLoot()
            .register();
    public static final BlockEntry<PunchcardWriterBlock> PUNCHCARD_WRITER = REGISTRATE.block("punchcard_writer", PunchcardWriterBlock::new)
            .initialProperties(DefaultProperties::brassMetal)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .transform(customItemModel())
            .addLayer(() -> RenderType::translucent)
            .defaultLoot()
            .register();

    public static final BlockEntry<? extends Block> LIQUID_TANK = REGISTRATE.block("liquid_tank", (block) -> new LiquidTankBlock(block).setBaseShape(Shapes.block()))
            .initialProperties(DefaultProperties::glass)
            .properties(p -> p.color(MaterialColor.METAL))
            .addLayer(() -> RenderType::cutout)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SteamFurnaceBlock> BOILER_FURNACE = REGISTRATE.block("steam_furnace", SteamFurnaceBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.lightLevel(SteamFurnaceBlock::getLightPower).strength(3.6F).sound(SoundType.NETHERITE_BLOCK))
            .transform(pickaxeOnly())
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .addLayer(() -> RenderType::cutoutMipped)
            .defaultLoot()
            .register();
    public static void register() {
        Create.registrate().addToSection(BELLOWS, AllSections.KINETICS);
        Create.registrate().addToSection(SPRINKLER, AllSections.KINETICS);
    }

    public static BlockEntry<?> createHorizontal(String name, NonNullFunction<BlockBehaviour.Properties, ? extends Block> factory, NonNullSupplier<? extends Block> properties) {
        return REGISTRATE.block(name, factory)
                .initialProperties(properties)
                .blockstate(BlockStateGen.horizontalBlockProvider(true))
                .register();
    }
}
