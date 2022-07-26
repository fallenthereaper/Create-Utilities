package com.fallenreaper.createutilities.content.blocks.bellow;

import com.fallenreaper.createutilities.CreateUtilities;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import joptsimple.internal.Strings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class BellowDataHelper {
//copied from create i have no idea whats gioing on here
    public static MutableComponent barComponent(int level, int minValue, int maxValue) {
        return TextComponent.EMPTY.copy()
                .append(bars(Math.max(0, minValue - 1), ChatFormatting.DARK_GREEN))
                .append(bars(minValue > 0 ? 1 : 0, ChatFormatting.GREEN))
                .append(bars(Math.max(0, level - minValue), ChatFormatting.DARK_GREEN))
                .append(bars(Math.max(0, maxValue - level), ChatFormatting.DARK_RED))
                .append(bars(Math.max(0, Math.min(18 - maxValue, ((maxValue / 5 + 1) * 5) - maxValue)),
                        ChatFormatting.DARK_GRAY));

    }

    private static MutableComponent bars(int level, ChatFormatting format) {
        return new TextComponent(Strings.repeat('|', level)).withStyle(format);
    }
    public static MutableComponent getFillComponent(boolean forGoggles, boolean useBlocksAsBars, ChatFormatting... styles) {
        return componentHelper("fillLevel", 5, forGoggles, useBlocksAsBars, styles);
    }

    @NotNull
    public static MutableComponent getHeatLevelTextComponent(int level) {
        int boilerLevel = Math.min(level, 16);
        LangBuilder amount = Lang.builder(CreateUtilities.ID);
        return  amount.translate("fillLevel.lvl", String.valueOf(boilerLevel)).component();
    }

    public static MutableComponent componentHelper(String label, int level, boolean forGoggles, boolean useBlocksAsBars,
                                             ChatFormatting... styles) {
        MutableComponent base = barComponent(level, 2, 10);

        if (!forGoggles)
            return base;

        ChatFormatting style1 = styles.length >= 1 ? styles[0] : ChatFormatting.GRAY;
        ChatFormatting style2 = styles.length >= 2 ? styles[1] : ChatFormatting.DARK_GRAY;

        LangBuilder amount = Lang.builder(CreateUtilities.ID);
        return amount.translate("bellow." + label).component()
                .withStyle(style1)
                .append(amount.translate("bellow." + label + "_dots").component()
                        .withStyle(style2))
                .append(base);
    }
}
