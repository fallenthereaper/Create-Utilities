package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;


public class TypewriterRenderer extends SmartTileEntityRenderer<TypewriterBlockEntity> {


    public TypewriterRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(TypewriterBlockEntity tileEntityIn, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        var blockState = tileEntityIn.getBlockState();
        var facing = blockState.getValue(TypewriterBlock.HORIZONTAL_FACING);
        if (tileEntityIn.hasBlueprintIn() || !tileEntityIn.inventory.getStackInSlot(4).isEmpty()) {
            SuperByteBuffer blueprint = CachedBufferer.partial(CUBlockPartials.SCHEMATIC_MODEL, blockState);
            rotateCenteredInDirection(blueprint, Direction.UP, facing);
            blueprint.renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
        VertexConsumer builder = buffer.getBuffer(RenderType.cutoutMipped());
    }

    public static double getAngleForFacing(Direction facing) {
        return 90 * (facing.equals(Direction.NORTH) ? 4 : facing.equals(Direction.SOUTH) ? 2 : facing.equals(Direction.EAST) ? 3 : 1);
    }

    public static void rotateCenteredInDirection(SuperByteBuffer model, Direction direction, Direction facing) {
        model.rotateCentered(direction, (float) Math.toRadians(getAngleForFacing(facing)));
    }
}