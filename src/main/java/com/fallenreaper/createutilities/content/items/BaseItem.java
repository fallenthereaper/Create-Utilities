package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.utils.data.IThirdPersonAnimation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;


public class BaseItem extends Item implements IThirdPersonAnimation {
    public BaseItem(Properties pProperties) {
        super(pProperties);

    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.UNBREAKING || enchantment == Enchantments.MENDING;
    }


    @Override
    public <T extends LivingEntity> boolean poseArm(ItemStack itemStack, HumanoidArm arm, HumanoidModel<T> model, T entity, boolean rightHand) {
        return false;
    }
}




