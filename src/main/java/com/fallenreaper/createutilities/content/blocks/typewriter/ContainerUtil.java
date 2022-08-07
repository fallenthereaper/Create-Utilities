package com.fallenreaper.createutilities.content.blocks.typewriter;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

public class ContainerUtil {
  static final Random RANDOM = new Random();

    public static void dropContents(Level pLevel, BlockPos pPos, ItemStackHandler te) {

        for(int i = 0; i < te.getSlots(); i++) {
        ItemStack item = te.getStackInSlot(i);
            dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), item);
        }
    }

    public static void dropItemStack(Level pLevel, double pX, double pY, double pZ, ItemStack pStack) {
        double d0 = EntityType.ITEM.getWidth();
        double d1 = 1.0D - d0;
        double d2 = d0 / 2.0D;
        double d3 = Math.floor(pX) + RANDOM.nextDouble() * d1 + d2;
        double d4 = Math.floor(pY) + RANDOM.nextDouble() * d1;
        double d5 = Math.floor(pZ) + RANDOM.nextDouble() * d1 + d2;

        while(!pStack.isEmpty()) {
            ItemEntity itementity = new ItemEntity(pLevel, d3, d4, d5, pStack.split(RANDOM.nextInt(21) + 10));
            float f = 0.05F;
            itementity.setDeltaMovement(RANDOM.nextGaussian() * (double)0.05F, RANDOM.nextGaussian() * (double)0.05F + (double)0.2F, RANDOM.nextGaussian() * (double)0.05F);
            pLevel.addFreshEntity(itementity);
        }

    }
}
