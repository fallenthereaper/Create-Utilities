package com.fallenreaper.createutilities.mixins;

import com.fallenreaper.createutilities.core.data.IThirdPersonAnimation;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("all")
@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> {

    @Inject(method = "poseRightArm", at = @At(value = "HEAD"), cancellable = true, require = 0)
    public void poseRightArm(T entity, CallbackInfo ci) {

        HumanoidArm handSide = entity.getMainArm();
        ItemStack stack = entity.getItemInHand(handSide == HumanoidArm.RIGHT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
        if (stack.getItem() instanceof IThirdPersonAnimation thirdPersonAnimation) {
            if (thirdPersonAnimation.poseArm(stack, handSide, (HumanoidModel<T>) (Object) this, entity, true)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "poseLeftArm", at = @At(value = "HEAD"), cancellable = true, require = 0)
    public void poseLeftArm(T entity, CallbackInfo ci) {

        HumanoidArm handSide = entity.getMainArm();
        ItemStack stack = entity.getItemInHand(handSide == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        Item item = stack.getItem();
        if (item instanceof IThirdPersonAnimation thirdPersonAnimation) {
            if (thirdPersonAnimation.poseArm(stack, handSide, (HumanoidModel<T>) (Object) this, entity, false)) {
                ci.cancel();
            }
        }
    }

}
