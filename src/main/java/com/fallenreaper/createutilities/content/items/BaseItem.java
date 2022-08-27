package com.fallenreaper.createutilities.content.items;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;


public class BaseItem extends Item {
  protected CompoundTag compoundTag  = new CompoundTag();
    private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);
    public BaseItem(Properties pProperties) {
        super(pProperties);

    }
    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return pStack.hasTag() && pStack.getItem() instanceof NotesItem;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Minecraft mc = Minecraft.getInstance();


        return InteractionResultHolder.success((pPlayer.getItemInHand(pUsedHand)));
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



  /*
        String spacing = "    ";

        Component indent = new TextComponent(spacing + " ");
        TranslatableComponent description = new TranslatableComponent(this.toString());
        Component percentage = new TextComponent((String.valueOf(getFromTag("clicks")))).withStyle(ChatFormatting.GREEN);
        pTooltipComponents.add(indent.plainCopy()
                .append(description.getKey()+ " " + "Info:").append(percentage));
*/

    }




