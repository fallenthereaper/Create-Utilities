package com.fallenreaper.createutilities.core.data;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Implement this interface if you want to override the default item animation.
 * Only gets called before rendering the item in hand.
 */
public interface IFirstPersonAnimation {
    /**
     * Gets called before rendering the item in hand.
     *
     * @param entity       the entity
     * @param stack        the stack
     * @param hand         the hand
     * @param matrixStack  the matrix stack
     * @param partialTicks the partial ticks
     * @param pitch        the pitch
     * @param attackAnim   the attack anim
     * @param handHeight   the hand height
     */
    void animateItem(final LivingEntity entity, final ItemStack stack, final InteractionHand hand, final PoseStack matrixStack,
                                float partialTicks, float pitch, float attackAnim, float handHeight);
}
