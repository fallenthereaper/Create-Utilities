package com.fallenreaper.createutilities.core.data;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
@FunctionalInterface
public interface IThirdPersonAnimation {
    <T extends LivingEntity> boolean poseArm(ItemStack itemStack, HumanoidArm arm, HumanoidModel<T> model, T entity, boolean rightHand);

}
