package com.fallenreaper.createutilities.content.armor;

import com.fallenreaper.createutilities.CreateUtilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BrassSuitArmorItem extends ArmorItem {

    public static final ResourceLocation TEXTURE = CreateUtilities.defaultResourceLocation("textures/models/armor/brass_jetpack.png");
    private static final String TEXTURE_STRING = TEXTURE.toString();

    public BrassSuitArmorItem(EquipmentSlot p_i48534_2_, Properties p_i48534_3_) {
        super(ArmorMaterials.BRASS_JETPACK, p_i48534_2_, p_i48534_3_.stacksTo(1));
    }


    public boolean isWornBy(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity))
            return false;
        return livingEntity.getItemBySlot(slot).getItem() == this;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return TEXTURE_STRING;
    }
}
