package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.bellow.BellowBlock;
import com.fallenreaper.createutilities.content.blocks.sliding_door.ModifiedSlidingDoor;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlock;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterBlock;
import com.fallenreaper.createutilities.utils.DefaultProperties;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.AllTags.pickaxeOnly;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class CUBlocks {
    private static final CreateRegistrate REGISTRATE = CreateUtilities.registrate().creativeModeTab(() -> CreateUtilities.TAB);

    public static final BlockEntry<SprinklerBlock> SPRINKLER = REGISTRATE.block("sprinkler", SprinklerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .transform(customItemModel())
            .addLayer(() -> RenderType::cutoutMipped)
            .defaultLoot()
            .register();
   public static final BlockEntry<ModifiedSlidingDoor> BRASS_DOOR = REGISTRATE.block("brass_door", ModifiedSlidingDoor::new)
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
    public static final BlockEntry<BellowBlock> BELLOWS = REGISTRATE.block("bellow", BellowBlock::new)
            .initialProperties(DefaultProperties::brassMetal)
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .transform(customItemModel())
            .addLayer(() -> RenderType::cutoutMipped)
            .defaultLoot()
            .register();

    public static void register() {
        Create.registrate().addToSection(BELLOWS, AllSections.KINETICS);
        Create.registrate().addToSection(SPRINKLER, AllSections.KINETICS);
    }
}
