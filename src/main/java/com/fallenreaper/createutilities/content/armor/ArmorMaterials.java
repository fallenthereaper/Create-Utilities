package com.fallenreaper.createutilities.content.armor;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.world.item.crafting.Ingredient;

public class ArmorMaterials {
   public static ModArmorMaterial BRASS_JETPACK = ModArmorMaterial.create("brass_jetpack",3, new int[] { 2, 6, 8, 4 }, 25 / 2, AllSoundEvents.COPPER_ARMOR_EQUIP.getMainEvent(), 0.25F, 0.25F,
            () -> Ingredient.of(AllItems.BRASS_INGOT.get(), AllItems.ANDESITE_ALLOY.get()));
}
