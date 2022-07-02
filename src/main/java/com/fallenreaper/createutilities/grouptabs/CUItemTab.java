package com.fallenreaper.createutilities.grouptabs;

import com.fallenreaper.createutilities.index.CUBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

public class CUItemTab {
   public static TextComponent text = new TextComponent("Create Utilities");
   // public static Supplier<ItemStack> provider = () -> CUBlocks.SPRINKLER.asStack();

    public static ModItemTab MAIN_GROUP = new ModItemTab(text.getText()) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return CUBlocks.TYPEWRITER.asStack();
        }

    };


}
