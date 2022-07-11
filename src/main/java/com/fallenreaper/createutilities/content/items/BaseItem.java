package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.content.blocks.sprinkler.SprinklerBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseItem extends Item implements TypewriterProvider {
    int clicks;
  protected CompoundTag compoundTag  = new CompoundTag();
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);
    public BaseItem(Properties pProperties) {
        super(pProperties);

    }
    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return getFromTag("clicks")  > 0;
    }
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        Player player = pContext.getPlayer();

        BlockState blockState = pContext.getLevel().getBlockState(pos);

        if (blockState.getBlock() instanceof SprinklerBlock) {

            this.clicks++;
            addToTag("clicks", clicks);
            return InteractionResult.CONSUME;
        }
        else
        if (blockState.getBlock() instanceof GrassBlock) {
        if((clicks > 0))
          this.clicks--;
            addToTag("clicks", clicks);
            return InteractionResult.CONSUME;
        }


        return super.useOn(pContext);
    }


    public void addToTag(String key, int tag) {
        compoundTag.putInt(key, Math.min(tag, getMaxClicks()));
    }
    public int getFromTag(String key) {
        if(!compoundTag.contains(key))
            return 0;

        return compoundTag.getInt(key);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING;
    }

    @Override
    public int getBarWidth(ItemStack pStack) {
        return Math.min( (13 * getFromTag("clicks") / getMaxClicks()), 13);
    }
    @Override
    public int getBarColor(ItemStack pStack) {
        float f = Math.max(0.0F,  (float)getFromTag("clicks")) / getMaxClicks();
        return  Mth.hsvToRgb(f / 2.0F, 2.0F, 0.45F);
    }
    public int getMaxClicks() {
        return 64;
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        String spacing = "    ";

        Component indent = new TextComponent(spacing + " ");
        TranslatableComponent description = new TranslatableComponent(this.toString());
        Component percentage = new TextComponent((String.valueOf(getFromTag("clicks")))).withStyle(ChatFormatting.GREEN);
        pTooltipComponents.add(indent.plainCopy()
                .append(description.getKey()+ " " + "Info:").append(percentage));

    }
}
