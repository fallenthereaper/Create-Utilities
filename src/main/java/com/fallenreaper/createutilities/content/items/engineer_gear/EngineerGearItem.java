package com.fallenreaper.createutilities.content.items.engineer_gear;

import com.fallenreaper.createutilities.index.CUArmorMaterials;
import com.fallenreaper.createutilities.content.items.GenericArmorItem;
import net.minecraft.world.entity.EquipmentSlot;


public class EngineerGearItem extends GenericArmorItem {
    public EngineerGearItem(Properties pProperties, EquipmentSlot slot) {
        super(CUArmorMaterials.ENGINEER_GEAR, slot, pProperties);
    }
}
