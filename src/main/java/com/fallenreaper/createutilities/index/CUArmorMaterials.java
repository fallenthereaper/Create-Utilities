package com.fallenreaper.createutilities.index;

import com.fallenreaper.createutilities.content.armor.ArmorMaterials;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class CUArmorMaterials {
    public static ArmorMaterials BRASS_JETPACK = ArmorMaterials.create("brass_jetpack", 3, new int[]{2, 6, 8, 4}, 25 / 4, AllSoundEvents.COPPER_ARMOR_EQUIP.getMainEvent(), 0.25F, 0.25F,
            () -> Ingredient.of(AllItems.BRASS_INGOT.get(), AllItems.ANDESITE_ALLOY.get()));

    public static ArmorMaterials ENGINEER_GEAR = ArmorMaterials.create("engineer_gear", 3, new int[]{1, 3, 4, 2}, 25 / 2, AllSoundEvents.COPPER_ARMOR_EQUIP.getMainEvent(), 0.25F, 0.0F,
            () -> Ingredient.of(AllItems.GOGGLES.get(), Items.LEATHER, Items.LEATHER_HELMET));
}
