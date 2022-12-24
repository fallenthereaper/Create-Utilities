package com.fallenreaper.createutilities.core.client;// Made with Blockbench 4.3.1
// Exported for Minecraft version 1.17 - 1.18 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BrassJetPackModel extends BaseArmorModel {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("createutilities", "brass_jetpack"), "main");


    public BrassJetPackModel(ModelPart root) {
        super(root);

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0), 0.0F);
        PartDefinition partdefinition = createBaseArmorModel(meshdefinition);
        PartDefinition body = partdefinition.getChild("body");


        PartDefinition jetpack = body.addOrReplaceChild("jetpack", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-4.0F, 5.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(-0.25F))
                .texOffs(28, 49).addBox(-4.0F, 9.0F, -3.0F, 8.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(29, 9).addBox(-4.0F, -1.0F, 2.0F, 8.0F, 7.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));
           jetpack.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(23, 19).addBox(-3.0F, -4.0F, -3.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(4.0F, -4.0F, -3.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 4.0F, 5.0F, 0.3927F, 0.0F, 0.0F));
      body.addOrReplaceChild("meter", CubeListBuilder.create().texOffs(32, 43).addBox(-1.0F, -23.0F, -4.25F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 43).addBox(-2.0F, -24.0F, -5.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(2, 53).addBox(-2.0F, -24.0F, -4.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 30.0F, 1.2F));


        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull LivingEntity pEntity, float limbSwing, float limbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
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

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}