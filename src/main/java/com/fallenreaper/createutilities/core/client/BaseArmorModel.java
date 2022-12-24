package com.fallenreaper.createutilities.core.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;

public class BaseArmorModel extends HumanoidModel {
    public ModelPart root, head, body, leftLeg, rightLeg, leftFoot, rightFoot, torso, leftArm, rightArm,rightKneePlate, leftKneePlate ;
    protected EquipmentSlot equipmentSlot;

    public BaseArmorModel(ModelPart pRoot) {
        super(pRoot);
        this.root = pRoot;
        this.head = pRoot.getChild("head");
        this.body = pRoot.getChild("body");
        this.torso = pRoot.getChild("torso");
        this.leftArm = pRoot.getChild("left_arm");
        this.rightArm = pRoot.getChild("right_arm");
        this.rightFoot = pRoot.getChild("right_foot");
        this.leftFoot = pRoot.getChild("left_foot");
        this.rightLeg = pRoot.getChild("right_leg");
        this.leftLeg = pRoot.getChild("left_leg");
        this.rightKneePlate = pRoot.getChild("right_knee_plate");
        this.leftKneePlate = pRoot.getChild("left_knee_plate");
    }

    public static PartDefinition createBaseArmorModel(MeshDefinition mesh) {
        PartDefinition modelRoot = mesh.getRoot();
        modelRoot.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        modelRoot.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        modelRoot.addOrReplaceChild("left_foot", CubeListBuilder.create(), PartPose.ZERO);
        modelRoot.addOrReplaceChild("right_foot", CubeListBuilder.create(), PartPose.ZERO);
        modelRoot.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        modelRoot.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
        modelRoot.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.ZERO);
        modelRoot.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
        modelRoot.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
        modelRoot.addOrReplaceChild("right_knee_plate",CubeListBuilder.create(), PartPose.ZERO );
        modelRoot.addOrReplaceChild("left_knee_plate",CubeListBuilder.create(), PartPose.ZERO );

        return modelRoot;
    }



    public void setEquipmentSlot(EquipmentSlot equipmentSlot) {
        this.equipmentSlot = equipmentSlot;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return getEquipmentSlot() == EquipmentSlot.HEAD ? ImmutableList.of(head) : ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        switch (getEquipmentSlot()) {
            case CHEST -> { return ImmutableList.of(body, leftArm, rightArm, torso); }
            case LEGS -> { return ImmutableList.of(rightLeg, leftLeg, rightKneePlate, leftKneePlate);}
            case FEET -> { return ImmutableList.of(rightFoot, leftFoot);}
            default -> { return ImmutableList.of();}
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        super.renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void offset(HumanoidModel model) {
         torso.copyFrom(model.body);
        leftKneePlate.copyFrom(model.leftLeg);
        rightKneePlate.copyFrom(model.rightLeg);
        rightFoot.copyFrom(model.rightLeg);
        leftFoot.copyFrom(model.leftLeg);
    }

    @Override
    public void setupAnim(Entity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }
}
