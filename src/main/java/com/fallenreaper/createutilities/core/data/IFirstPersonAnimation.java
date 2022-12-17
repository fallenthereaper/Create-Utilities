package com.fallenreaper.createutilities.core.data;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IFirstPersonAnimation {
    void animateItem(final LivingEntity entity, final ItemStack stack, final InteractionHand hand, final PoseStack matrixStack,
                                float partialTicks, float pitch, float attackAnim, float handHeight);
}
