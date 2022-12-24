package com.fallenreaper.createutilities.core.events;

import com.fallenreaper.createutilities.core.client.BrassJetPackModel;
import com.fallenreaper.createutilities.core.client.EngineerGearModel;
import com.fallenreaper.createutilities.index.CUItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import static com.fallenreaper.createutilities.core.client.CUModelLayers.BRASS_JETPACK_LAYER;
import static com.fallenreaper.createutilities.core.client.CUModelLayers.ENGINEER_GEAR_LAYER;

public class ArmorRenderProperties implements IClientItemExtensions {

    private static boolean init;

    public static EngineerGearModel ENGINEER_GEAR_MODEL = null;
    public static BrassJetPackModel BRASS_JET_PACK_MODEL = null;

    public static void initializeModels(LivingEntity entity) {
        init = true;
        ENGINEER_GEAR_MODEL = new EngineerGearModel(Minecraft.getInstance().getEntityModels().bakeLayer(ENGINEER_GEAR_LAYER));

       BRASS_JET_PACK_MODEL = new BrassJetPackModel(Minecraft.getInstance().getEntityModels().bakeLayer(BRASS_JETPACK_LAYER));
    }

    @Override
    public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
        if (!init) {
            initializeModels(entity);
        }

        float pticks = Minecraft.getInstance().getFrameTime();
        float f = Mth.rotLerp(pticks, entity.yBodyRotO, entity.yBodyRot);
        float f1 = Mth.rotLerp(pticks, entity.yHeadRotO, entity.yHeadRot);
        float netHeadYaw = f1 - f;
        float netHeadPitch = Mth.lerp(pticks, entity.xRotO, entity.getXRot());

        if (CUItems.ENGINEER_TUNIC.isIn(itemStack)) {
            if (ENGINEER_GEAR_MODEL != null) {
                ENGINEER_GEAR_MODEL.setupAnim(entity, entity.animationPosition, entity.animationSpeed, entity.tickCount + pticks, netHeadYaw, netHeadPitch);
                ENGINEER_GEAR_MODEL.setEquipmentSlot(EquipmentSlot.CHEST);
                ENGINEER_GEAR_MODEL.offset(_default);
                return ENGINEER_GEAR_MODEL;
            }
        }
        if (CUItems.ENGINEER_BOOTS.isIn(itemStack)) {
            if (ENGINEER_GEAR_MODEL != null) {
                ENGINEER_GEAR_MODEL.setupAnim(entity, entity.animationPosition, entity.animationSpeed, entity.tickCount + pticks, netHeadYaw, netHeadPitch);
                ENGINEER_GEAR_MODEL.setEquipmentSlot(EquipmentSlot.FEET);
                ENGINEER_GEAR_MODEL.offset(_default);
                return ENGINEER_GEAR_MODEL;
            }
        }
            if (CUItems.ENGINEER_TOP_HAT.isIn(itemStack)) {
                if (ENGINEER_GEAR_MODEL != null) {
                    ENGINEER_GEAR_MODEL.setupAnim(entity, entity.animationPosition, entity.animationSpeed, entity.tickCount + pticks, netHeadYaw, netHeadPitch);
                    ENGINEER_GEAR_MODEL.setEquipmentSlot(EquipmentSlot.HEAD);
                    ENGINEER_GEAR_MODEL.offset(_default);
                    return ENGINEER_GEAR_MODEL;
                }
            }
            if (CUItems.BRASS_JETPACK.isIn(itemStack)) {
                if (BRASS_JET_PACK_MODEL != null) {
                    BRASS_JET_PACK_MODEL.setEquipmentSlot(EquipmentSlot.CHEST);
                    BRASS_JET_PACK_MODEL.offset(_default);
                    BRASS_JET_PACK_MODEL.setupAnim(entity, entity.animationPosition, entity.animationSpeed, entity.tickCount + pticks, netHeadYaw, netHeadPitch);
                    return BRASS_JET_PACK_MODEL;
                }
            }
            return _default;
        }
}