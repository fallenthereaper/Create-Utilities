package com.fallenreaper.createutilities.content.blocks.bellow;

import com.fallenreaper.createutilities.CreateUtilities;
import com.simibubi.create.content.logistics.block.display.DisplayLinkContext;
import com.simibubi.create.content.logistics.block.display.source.NumericSingleLineDisplaySource;
import com.simibubi.create.content.logistics.block.display.target.DisplayTargetStats;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BellowDisplaySource extends NumericSingleLineDisplaySource {
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
       if (!(context.getSourceTE() instanceof BellowBlockEntity te))
        return EMPTY_LINE;

        if(!te.isValid || te.getSpeed() == 0 )
            return EMPTY_LINE;

        MutableComponent time = new TextComponent(te.getTotalTime(te.getMaxBurnTime(te.itemIn)));


        return time;
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        super.initConfigurationWidgets(context, builder, isFirstLine);
        if (isFirstLine)
            return;
       LangBuilder lang = Lang.builder(CreateUtilities.ID);
        builder.addSelectionScrollInput(0, 60, (si, l) -> {
            si.forOptions(lang.translate("display_source.bellow.item", "display_source.bellow.show_item_count", "display_source.show_item_name").component().getSiblings())
                    .titled(lang.translate("display_source_bellow_title").component());
        }, "Cycle");
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
    @Override
    protected String getTranslationKey() {
        return "time_duration";
    }

}
