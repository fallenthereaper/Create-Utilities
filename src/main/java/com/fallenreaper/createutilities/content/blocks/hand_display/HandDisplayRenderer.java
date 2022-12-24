package com.fallenreaper.createutilities.content.blocks.hand_display;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.foundation.tileEntity.renderer.SafeTileEntityRenderer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static com.simibubi.create.content.contraptions.base.DirectionalKineticBlock.FACING;

public class HandDisplayRenderer extends SafeTileEntityRenderer<HandDisplayBlockEntity> {
    public HandDisplayRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }


    @Override
    protected void renderSafe(HandDisplayBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        ItemStack heldItem = te.getHeldItem();
        if (heldItem.isEmpty())
            return;
        BlockState blockState = te.getBlockState();
        Direction facing = blockState.getValue(HandDisplayBlock.FACING);


        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();
        boolean blockItem = itemRenderer.getModel(heldItem, null, null, 0)
                .isGui3d() || heldItem.getItem() instanceof TieredItem;

        ms.pushPose();
        ItemTransforms.TransformType transform = ItemTransforms.TransformType.NONE;

        TransformStack.cast(ms)
                .centre()
                .translate(0, 1/16f, 0);

        float scale = blockItem && !(heldItem.getItem() instanceof TieredItem) ? 1.0F : 0.65F;
        transform = ItemTransforms.TransformType.FIXED;
        ms.scale(scale, scale, scale);
        float yRot = AngleHelper.horizontalAngle(facing) + 180;
        ms.mulPose(Vector3f.YP.rotationDegrees(yRot));
        if(te.isStaticDisplay()) {
            ms.translate(0, 0.35F, 0);
        }
        if(te.isAnimatedDisplay()) {
            ms.translate(0,  blockItem ? (Math.sin(AnimationTickHolder.getRenderTime(te.getLevel()) * 1 / 10.0F) / 5.0F) + 0.5f : 6.0F /16.0F, 0);

            ms.mulPose(Vector3f.YP.rotationDegrees(AnimationTickHolder.getRenderTime(te.getLevel())));
            if(blockItem && !(heldItem.getItem() instanceof TieredItem))ms.mulPose(Vector3f.ZP.rotationDegrees(-45));
        }

        itemRenderer.renderStatic(heldItem, transform, light, overlay, ms, bufferSource, 0);
        ms.popPose();


  /*
        matrixStackIn.pushPose();
        float f2 = ((float) podium.prevTicksExisted + (podium.ticksExisted - podium.prevTicksExisted) * partialTicks);
        float f3 = Mth.sin(f2 / 10.0F) * 0.1F + 0.1F;
        matrixStackIn.translate(0.5F, 1.55F + f3, 0.5F);
        float f4 = (f2 / 20.0F) * (180F / (float) Math.PI);
        matrixStackIn.mulPose(new Quaternion(Vector3f.YP, f4, true));
        matrixStackIn.pushPose();
        matrixStackIn.translate(0, 0.2F, 0);
        matrixStackIn.scale(0.65F, 0.65F, 0.65F);
        Minecraft.getInstance().getItemRenderer().renderStatic(podium.getItem(0), ItemTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, 0);
        matrixStackIn.popPose();
        matrixStackIn.popPose();

   */
    }

    protected void renderItem(HandDisplayBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        if (te.heldItem.isEmpty()) return;

        BlockState deployerState = te.getBlockState();
        Vec3 offset = VecHelper.getCenterOf(BlockPos.ZERO);
        ms.pushPose();
        ms.translate(offset.x, offset.y, offset.z);

        Direction facing = deployerState.getValue(FACING);

        float yRot = AngleHelper.horizontalAngle(facing) + 180;
        float xRot = facing == Direction.UP ? 90 : facing == Direction.DOWN ? 270 : 0;
        boolean displayMode = facing == Direction.UP ;

        ms.mulPose(Vector3f.YP.rotationDegrees(yRot));
        if (!displayMode) {
            ms.mulPose(Vector3f.XP.rotationDegrees(xRot));
            ms.translate(0, 0, -11 / 16f);
        }



        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();

        ItemTransforms.TransformType transform = ItemTransforms.TransformType.NONE;
        boolean isBlockItem = (te.heldItem.getItem() instanceof BlockItem)
                && itemRenderer.getModel(te.heldItem, te.getLevel(), null, 0)
                .isGui3d();

        if (displayMode) {
            float scale = isBlockItem ? 1.25f : 1;
            ms.translate(0, isBlockItem ? 9 / 16f : 11 / 16f, 0);
            ms.scale(scale, scale, scale);
            transform = ItemTransforms.TransformType.GROUND;
            ms.mulPose(Vector3f.YP.rotationDegrees(AnimationTickHolder.getRenderTime(te.getLevel())));

        }

        itemRenderer.renderStatic(te.heldItem, transform, light, overlay, ms, buffer, 0);
        ms.popPose();
    }
}
