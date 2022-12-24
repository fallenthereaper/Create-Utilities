package com.fallenreaper.createutilities.content.items.brass_jetpack;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.index.CUArmorMaterials;
import com.fallenreaper.createutilities.content.items.GenericArmorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

public class BrassSuitArmorItem extends GenericArmorItem {

    public static final ResourceLocation TEXTURE = CreateUtilities.defaultResourceLocation("textures/models/armor/brass_jetpack.png");
    private static final String TEXTURE_STRING = TEXTURE.toString();

    public BrassSuitArmorItem(EquipmentSlot p_i48534_2_, Properties p_i48534_3_) {
        super(CUArmorMaterials.BRASS_JETPACK, p_i48534_2_, p_i48534_3_);
    }
}
