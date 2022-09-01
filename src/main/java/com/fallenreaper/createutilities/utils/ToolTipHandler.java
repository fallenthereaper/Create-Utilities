package com.fallenreaper.createutilities.utils;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo wip
public class ToolTipHandler {
    public static Map<String, List<Component>> savedToolTips;
    private final Component DEFAULT = TextComponent.EMPTY;

    public static void registerToolTip(List<Component> tooltips, ItemStack itemStack, Player player, TextComponent key) {
        savedToolTips = new HashMap<>();


        if (!savedToolTips.containsKey(key.toString()))
            add(key, tooltips);

    //    tooltips.clear();
       // tooltips.add(new TextComponent("Hold shift to show more").withStyle(ChatFormatting.DARK_GRAY));



    }

    public static void add(TextComponent key, List<Component> toolTips) {
        savedToolTips.put(key.toString(), toolTips);
    }
    public static void showHiddenText() {


    }

    public static void remove(TextComponent key) {
        savedToolTips.remove(key.toString());
    }

    public static boolean hasSavedToolTip(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IHaveHiddenToolTip iHaveHiddenToolTip) {
            return savedToolTips.containsKey(iHaveHiddenToolTip.getKey().toString());
        }

        return findTooltip(itemStack);
    }

    private static boolean findTooltip(ItemStack stack) {
        if (stack.getItem() instanceof IHaveHiddenToolTip iHaveHiddenToolTip) {
            String key = iHaveHiddenToolTip.getKey().toString();
            if (I18n.exists(key)) {


                return true;
            }
            savedToolTips.put(key, null);
        }
        return false;
    }
}
