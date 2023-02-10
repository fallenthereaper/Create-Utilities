package com.fallenreaper.createutilities.core.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


public class EngineerGearModel extends BaseArmorModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor

    public EngineerGearModel(ModelPart root) {
        super(root);

    }

    public static LayerDefinition createArmorLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0), 0);
        PartDefinition root = createBaseArmorModel(meshdefinition);

        PartDefinition head = root.getChild("head");
        PartDefinition body = root.getChild("body");
        PartDefinition right_foot = root.getChild("right_foot");
        PartDefinition left_foot = root.getChild("left_foot");
        PartDefinition left_arm = root.getChild("left_arm");
        PartDefinition right_arm = root.getChild("right_arm");


          //HEAD
        PartDefinition top_hat = head.addOrReplaceChild("top_hat", CubeListBuilder.create().texOffs(0, 29).addBox(-4.0F, -6.8197F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
                .texOffs(0, 0).addBox(-7.0F, 1.7803F, -7.0F, 14.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.7803F, 0.0F));
          top_hat.addOrReplaceChild("hat_r1", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, 0.0F, -7.0F, 3.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0007F, 1.78F, 0.0F, 0.0F, 0.0F, -0.7854F));
          top_hat.addOrReplaceChild("hat_r2", CubeListBuilder.create().texOffs(28, 14).addBox(-3.0F, 0.0003F, -7.0F, 3.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0007F, 1.78F, 0.0F, 0.0F, 0.0F, 0.7854F));
        PartDefinition goggles = top_hat.addOrReplaceChild("goggles", CubeListBuilder.create().texOffs(0, 14).addBox(-5.0F, 0.35F, -5.5F, 10.0F, 4.0F, 11.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -3.8197F, -0.1F));
        top_hat.addOrReplaceChild("feather_r1", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -11.3146F, -1.5269F, 0.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 3.75F, 0.5F, -0.1545F, 0.1501F, 0.2618F));
          //BODY
        PartDefinition cape_body = body.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(32, 29).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offset(0.0F, 12.0F, 0.0F));
        PartDefinition cape_clock = cape_body.addOrReplaceChild("cape_cloak", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.5F, 2.45F, -0.2618F, 0.0F, 0.0F));
        cape_clock.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(38, 98).addBox(-7.0F, 10.0F, -5.95F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(4, 101).addBox(2.4F, 10.0F, -5.95F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.1F))
                .texOffs(68, 108).addBox(-5.8F, 10.0F, -2.95F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(1.8F, -10.6F, 0.0F, 0.2618F, 0.0F, 0.0F));
         //LEFT BOOT
        PartDefinition left_boot = left_foot.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(0, 45).addBox(-2.5F, 3.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.7F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        left_boot.addOrReplaceChild("boots_left", CubeListBuilder.create().texOffs(40, 84).addBox(-2.0F, 6.0F, -2.4F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.7F))
                .texOffs(90, 70).addBox(-2.0F, 2.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offset(0.0F, 3.0F, -2.0F));
         //RIGHT BOOT
        PartDefinition right_boot = right_foot.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(-2.5F, 3.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.7F)).mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));
        right_boot.addOrReplaceChild("boots_right", CubeListBuilder.create().texOffs(40, 84).mirror().addBox(-2.0F, -1.0F, -2.4F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.7F)).mirror(false)
                .texOffs(90, 70).mirror().addBox(-2.0F, -5.0F, 0.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.7F)).mirror(false), PartPose.offset(0.0F, 10.0F, -2.0F));
         //RIGHT ARM
        PartDefinition right_glove = right_arm.addOrReplaceChild("right_glove", CubeListBuilder.create(), PartPose.offset(-3.75F, 2.5F, 1.5F));
         right_glove.addOrReplaceChild("right_sleeve", CubeListBuilder.create().texOffs(48, 0).addBox(-3.5F, -3.3F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.6F))
                 .texOffs(94, 42).addBox(-3.5F, 5.7F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.55F)), PartPose.offset(-0.75F, 0.8F, -1.5F));
         //LEFT_ARM
        PartDefinition left_glove = left_arm.addOrReplaceChild("left_glove",  CubeListBuilder.create(), PartPose.offset(3.75F, 2.5F, 1.5F));
       left_glove.addOrReplaceChild("left_sleeve", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-0.5F, -3.3F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false)
               .texOffs(94, 42).mirror().addBox(-0.5F, 5.7F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.55F)).mirror(false), PartPose.offset(0.75F, 0.8F, -1.5F));

/*


        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(0.25F, -2.75F, -3.5F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false)
                .texOffs(28, 53).mirror().addBox(0.25F, 3.5F, -4.5F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.75F, 2.5F, 1.5F));

        PartDefinition right_foot = partdefinition.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(-2.5F, 5.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition boots_right = right_foot.addOrReplaceChild("boots_right", CubeListBuilder.create().texOffs(2, 81).mirror().addBox(-2.0F, -1.0F, -3.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(52, 24).mirror().addBox(-2.0F, -3.0F, 0.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(0.0F, 10.0F, -2.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 0).addBox(-4.25F, -2.75F, -3.5F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.4F))
                .texOffs(28, 53).addBox(-5.25F, 3.5F, -4.5F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.75F, 2.5F, 1.5F));


 */
/*
        PartDefinition top_hat = head.addOrReplaceChild("top_hat", CubeListBuilder.create().texOffs(0, 29).addBox(-4.0F, -6.8197F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(0, 0).addBox(-7.0F, 1.7803F, -7.0F, 14.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.7803F, 0.0F));

        PartDefinition hat_r1 = top_hat.addOrReplaceChild("hat_r1", CubeListBuilder.create().texOffs(28, 0).addBox(0.0F, 0.0F, -7.0F, 3.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0007F, 1.78F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition hat_r2 = top_hat.addOrReplaceChild("hat_r2", CubeListBuilder.create().texOffs(28, 14).addBox(-3.0F, 0.0003F, -7.0F, 3.0F, 0.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0007F, 1.78F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition goggles = top_hat.addOrReplaceChild("goggles", CubeListBuilder.create().texOffs(0, 14).addBox(-5.0F, 1.1F, -5.5F, 10.0F, 4.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.8197F, -0.1F));

        PartDefinition feather_r1 = goggles.addOrReplaceChild("feather_r1", CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -8.8146F, -1.5269F, 0.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 3.75F, 0.5F, -0.1545F, 0.1501F, 0.2618F));

        PartDefinition cape_torso = body.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(32, 29).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offset(0.0F, 12.0F, 0.0F));

        PartDefinition cape_clock = cape_torso.addOrReplaceChild("cape_clock", CubeListBuilder.create(), PartPose.offset(0.0F, -1.1F, 2.2F));

        PartDefinition body_r1 = cape_clock.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 10.0F, -6.5F, 1.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 53).addBox(2.0F, 10.0F, -6.5F, 1.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(48, 15).addBox(-6.0F, 10.0F, -2.5F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -10.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition right_clothing = right_arm.addOrReplaceChild("right_clothing", CubeListBuilder.create().texOffs(48, 0).addBox(-4.05F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.4F))
                .texOffs(28, 53).addBox(-4.85F, 5.4F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.1F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition left_boot = left_foot.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(0, 45).addBox(-2.0F, 5.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition boots2 = left_boot.addOrReplaceChild("boots2", CubeListBuilder.create().texOffs(2, 81).addBox(-0.7F, -2.6F, -4.4F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.1F))
                .texOffs(52, 24).addBox(-0.1F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition right_boot = right_foot.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(-3.0F, 5.0F, -3.0F, 5.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition boots3 = right_boot.addOrReplaceChild("boots3", CubeListBuilder.create().texOffs(2, 81).mirror().addBox(-4.5F, -2.6F, -4.4F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(52, 24).mirror().addBox(-3.9F, -5.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition left_clothing = left_arm.addOrReplaceChild("left_clothing", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(0.05F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.4F)).mirror(false)
                .texOffs(28, 53).mirror().addBox(-0.15F, 5.4F, -3.0F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));
*/
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(LivingEntity pEntity, float limbSwing, float limbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float f = 1.0F;
        if(!(pEntity instanceof Player player ))
            return;
        if( !pEntity.getLevel().isClientSide())
            return;
        if (pEntity.getFallFlyingTicks() > 4) {
            f = (float)pEntity.getDeltaMovement().lengthSqr();
            f = f / 0.2F;
            f = f * f * f;
        }
        if (f < 1.0F) {
            f = 1.0F;
        }
        if (getEquipmentSlot() == EquipmentSlot.CHEST) {
       //     body.getChild("cape").getChild("cape_cloak").xRot = 3F / 16f + Mth.abs(Mth.cos(limbSwing * 4 / 16F) * 8 / 16F * limbSwingAmount / f);
        }
        if (getEquipmentSlot() == EquipmentSlot.HEAD ) {
            ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
               head.getChild("top_hat").getChild("goggles").visible = !AllItems.GOGGLES.isIn(itemInHand);
        }
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}