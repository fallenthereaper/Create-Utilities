package com.fallenreaper.createutilities.content.items;


import net.minecraft.ChatFormatting;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NotesItem extends BaseItem {
    public NotesItem(Properties pProperties) {
        super(pProperties);
    }



    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            tooltip.add(new TextComponent(pStack.getTag().getString("Description")).withStyle(ChatFormatting.GREEN).append(" ").append("Pos:").append(NbtUtils.readBlockPos(pStack.getTag().getCompound("DoorPosition")).toString()).withStyle(ChatFormatting.YELLOW));

        }
    }
}
