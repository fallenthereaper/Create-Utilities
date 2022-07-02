package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlock;
import com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterBlock;
import com.fallenreaper.createutilities.grouptabs.CUItemTab;
import com.fallenreaper.createutilities.utils.DefaultProperties;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;

import static com.simibubi.create.AllTags.pickaxeOnly;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class CUBlocks {
    private static final CreateRegistrate REGISTRATE = CreateUtilities.registrate().creativeModeTab(() -> CUItemTab.MAIN_GROUP);

    public static final BlockEntry<SprinklerBlock> SPRINKLER = REGISTRATE.block("sprinkler", SprinklerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalAxisBlockProvider(true))
            .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
            .lang("sprinkler")
            .item().transform(customItemModel())
            .addLayer(() -> RenderType::cutoutMipped)
            .defaultLoot()
            .register();

    public static final BlockEntry<TypewriterBlock> TYPEWRITER = REGISTRATE.block("typewriter", TypewriterBlock::new)
            .initialProperties(DefaultProperties::brassMetal)
            .blockstate(BlockStateGen.horizontalAxisBlockProvider(true))
            .tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
            .lang("typewriter")
            .item().transform(customItemModel())
            .addLayer(() -> RenderType::cutoutMipped)
            .defaultLoot()
            .register();



    public static void register() {
    }
}
