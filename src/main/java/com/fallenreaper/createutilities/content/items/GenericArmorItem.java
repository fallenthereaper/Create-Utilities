package com.fallenreaper.createutilities.content.items;

import com.fallenreaper.createutilities.CreateUtilities;
import com.fallenreaper.createutilities.content.armor.ArmorMaterials;
import com.fallenreaper.createutilities.core.events.ClientHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
//TODO: make this extend BaseItem
public class GenericArmorItem extends ArmorItem {
    private final String textureName;
    public GenericArmorItem(ArmorMaterial pMaterial, EquipmentSlot pSlot, Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
        this.textureName = ((ArmorMaterials) pMaterial).getTextureName();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ClientHandler.getArmorRenderer());
    }

    protected String getTextureName() {
        return textureName;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return CreateUtilities.ID + ":textures/entity/" + (!getTextureName().contains(".png") ? getTextureName() + ".png" : getTextureName());
    }

    public boolean isWornBy(Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity))
            return false;
        return livingEntity.getItemBySlot(slot).getItem() == this;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public @NotNull Object getRenderPropertiesInternal() {
        return ClientHandler.getArmorRenderer();
    }
}
