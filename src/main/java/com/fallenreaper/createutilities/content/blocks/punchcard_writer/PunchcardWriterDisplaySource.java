package com.fallenreaper.createutilities.content.blocks.punchcard_writer;

import com.simibubi.create.content.logistics.block.display.DisplayLinkContext;
import com.simibubi.create.content.logistics.block.display.source.DisplaySource;
import com.simibubi.create.content.logistics.block.display.target.DisplayTargetStats;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class PunchcardWriterDisplaySource extends DisplaySource {
    @Override
    public List<MutableComponent> provideText(DisplayLinkContext context, DisplayTargetStats stats) {
        return EMPTY;
    }
}
