package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.content.items.parchment.ParchmentScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ParchmentItem extends BaseItem{
    public ParchmentItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if(pLevel.isClientSide()) {
            pPlayer.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0f, 1.0f);
            ScreenOpener.open(new ParchmentScreen());
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        }

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
