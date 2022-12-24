package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowBlock;
import com.fallenreaper.createutilities.content.blocks.encased_nixie_tube.EncasedNixieTubeBlock;
import com.fallenreaper.createutilities.content.blocks.encased_nixie_tube.EncasedNixieTubeGenerator;
import com.fallenreaper.createutilities.content.blocks.hand_display.HandDisplayBlock;
import com.fallenreaper.createutilities.content.blocks.mechanical_propeller.MechanicalPropellerBlock;
import com.fallenreaper.createutilities.content.blocks.punchcard_writer.PunchcardWriterBlock;
import com.fallenreaper.createutilities.content.blocks.sliding_door.LockSlidingDoor;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlock;
import com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlock;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterBlock;
import com.fallenreaper.createutilities.core.data.blocks.liquidtank.LiquidTankBlock;
import com.fallenreaper.createutilities.core.utils.DefaultProperties;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.Shapes;

import static com.simibubi.create.AllBlocks.ORANGE_NIXIE_TUBE;
import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CUBlocks {
    private static final CreateRegistrate REGISTRATE = CreateUtilities.registrate().creativeModeTab(() -> CreateUtilities.TAB);

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

            .blockstate(simpleCubeAll("liquid_tank/liquid_tank"))
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
    public static final BlockEntry<HandDisplayBlock> HAND_DISPLAY = REGISTRATE.block("hand_display", HandDisplayBlock::new)
            .initialProperties(AllBlocks.DEPLOYER)
            .transform(pickaxeOnly())
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .blockstate((c, p) -> p.horizontalFaceBlock(c.get(), AssetLookup.standardModel(c, p)))
            .item()
            .transform(customItemModel())
            .defaultLoot()
            .register();

    public static final BlockEntry<EncasedNixieTubeBlock> ENCASED_ORANGE_NIXIE_TUBE =
            REGISTRATE.block("encased_nixie_tube", p -> new EncasedNixieTubeBlock(p, DyeColor.ORANGE))
                    .initialProperties(SharedProperties::softMetal)
                    .properties(p -> p.lightLevel($ -> 5))
                    .properties(p -> p.color(DyeColor.ORANGE.getMaterialColor()))
                    .transform(pickaxeOnly())
                    .blockstate(new EncasedNixieTubeGenerator()::generate)
                    .addLayer(() -> RenderType::translucent)
                    //  .item()
                    //  .transform(customItemModel())
                    .register();
    public static final DyedBlockList<EncasedNixieTubeBlock> ENCASED_NIXIE_TUBES = new DyedBlockList<>(colour -> {
        if (colour == DyeColor.ORANGE)
            return ENCASED_ORANGE_NIXIE_TUBE;
        String colourName = colour.getSerializedName();
        return REGISTRATE.block(colourName + "_encased_nixie_tube", p -> new EncasedNixieTubeBlock(p, colour))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.color(colour.getMaterialColor()))
                .properties(p -> p.lightLevel($ -> 5))
                .transform(pickaxeOnly())
                .blockstate(new EncasedNixieTubeGenerator()::generate)
                .loot((p, b) -> p.dropOther(b, ORANGE_NIXIE_TUBE.get()))
                .addLayer(() -> RenderType::translucent)
                .register();
    });

    public static void register() {
        Create.REGISTRATE.addToSection(BELLOWS, AllSections.KINETICS);
        Create.REGISTRATE.addToSection(SPRINKLER, AllSections.KINETICS);
    }

    public static BlockEntry<?> createHorizontal(String name, NonNullFunction<BlockBehaviour.Properties, ? extends Block> factory, NonNullSupplier<? extends Block> properties) {
        return REGISTRATE.block(name, factory)
                .initialProperties(properties)
                .blockstate(BlockStateGen.horizontalBlockProvider(true))
                .register();
    }
}
