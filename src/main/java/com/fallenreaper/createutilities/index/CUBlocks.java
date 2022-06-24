package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.blocks.sprinkler.SprinklerBlock;
import com.fallenreaper.createutilities.grouptabs.CUItemTab;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class CUBlocks {
    private static final CreateRegistrate REGISTRATE = CreateUtilities.registrate().creativeModeTab(()-> CUItemTab.MAIN_GROUP);
    public static final BlockEntry<SprinklerBlock> SPRINKLER = REGISTRATE.block("sprinkler", SprinklerBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.horizontalAxisBlockProvider(true))
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag) //Dono what this tag means (contraption safe?).
            .item().transform(customItemModel())
            .register();
    public static void register() {
        //CreateAeronautics.registrate().addToSection(TORSION_SPRING, AllSections.KINETICS);
    }
}
