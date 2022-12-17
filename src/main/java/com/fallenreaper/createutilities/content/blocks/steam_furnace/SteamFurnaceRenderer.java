package com.fallenreaper.createutilities.content.blocks.steam_furnace;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.jozufozu.flywheel.repack.joml.Vector2d;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Constants;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;

import static com.fallenreaper.createutilities.content.blocks.steam_furnace.SteamFurnaceBlockEntity.getIndicatorProgress;
import static com.fallenreaper.createutilities.content.blocks.typewriter.TypewriterRenderer.getAngleForFacing;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class SteamFurnaceRenderer extends SmartTileEntityRenderer<SteamFurnaceBlockEntity> {
    public SteamFurnaceRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    protected void renderSafe(SteamFurnaceBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);
        BlockState blockState = te.getBlockState();
        var facing = blockState.getValue(HorizontalKineticBlock.HORIZONTAL_FACING);
        SuperByteBuffer indicator = CachedBufferer.partial(CUBlockPartials.PRESSURE_INDICATOR, te.getBlockState());
        //
        Direction direction = te.getBlockState()
                .getValue(HORIZONTAL_FACING);
        float pivot = (float) (3.5 / 16f);

        float dt = AnimationTickHolder.getPartialTicks();
        float progress = 0;
        if (te.getState().isProducing())
            dt *= -1;
        te.indicatorProgress = Mth.clamp((te.indicatorProgress + dt), -30, 30);
        progress = Mth.clamp(progress * -AnimationTickHolder.getPartialTicks(), 0, 1);

        int color = Color.mixColors(0x2C0300, 0xCD0000, getIndicatorProgress(te));
        indicator.rotateCentered(Direction.UP, (float) Math.toRadians(getAngleForFacing(facing)))
                .translate(8 / 16F, 11.5 / 16F, 0)
                .rotate(Direction.NORTH, 80 * Constants.DEG_TO_RAD * (getIndicatorProgress(te) ))
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));


        Minecraft instance = Minecraft.getInstance();
        // rotateCenteredInDirection(indicator, Direction.UP, facing);
        //  indicator.renderInto(ms, buffer.getBuffer(RenderType.solid()));
        PoseStack.Pose posestack$pose = ms.last();
        VertexConsumer vertexconsumer1 = new SheetedDecalTextureGenerator(instance.renderBuffers().crumblingBufferSource().getBuffer(ModelBakery.DESTROY_TYPES.get(5)), posestack$pose.pose(), posestack$pose.normal());
        VoxelShape shape = blockState.getShape(te.getLevel(), te.getBlockPos());

        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.lines());


      //  LevelRenderer.renderVoxelShape(ms, vertexconsumer, shape, instance.cameraEntity.getX(), instance.cameraEntity.getY(), instance.cameraEntity.getZ(), 1.0F, 1.0F, 1.0F, 1.0F);
       // instance.getBlockRenderer().renderBreakingTexture(blockState, te.getBlockPos(), te.getLevel(), ms, vertexconsumer1);
        renderFoodItems(te, partialTicks, ms, buffer, light, overlay);

    }

    public void renderFoodItems(SteamFurnaceBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Direction direction = te.getBlockState().getValue(HORIZONTAL_FACING).getOpposite();

        ItemStackHandler inventory = te.getFoodInventory();
        int posLong = (int) te.getBlockPos().asLong();

        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                ms.pushPose();

                // Center item above the stove
                ms.translate(8/16.0D, 16.2F/16F, 8/16.0D);

                // Rotate item to face the stove's front side
                float f = -direction.toYRot();
                ms.mulPose(Vector3f.YP.rotationDegrees(f));


                ms.mulPose(Vector3f.XP.rotationDegrees(90.0F + te.getRotation(i)/270F));


                Vector2d itemOffset = te.getItemOffset(i);
                float rotation = te.getRotation(i);
                ms.translate(itemOffset.x , itemOffset.y , 0.0D);
                ms.translate(-8/16D, -8/16D, 0.0D);
                ms.mulPose(Vector3f.ZN.rotationDegrees(-rotation + 45.0F));
                float size = 10 / 16F;
                ms.scale(size, size, size);


                if (te.getLevel() != null)
                    Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, ms, buffer, posLong + i);
                ms.popPose();
            }
        }
    }
}

