package com.fallenreaper.createutilities.content.blocks.typewriter;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.curiosities.toolbox.ToolboxBlock;
import com.simibubi.create.content.curiosities.toolbox.ToolboxTileEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.tileEntity.renderer.SmartTileEntityRenderer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;

import javax.naming.directory.DirContext;

public class TypewriterRenderer extends SmartTileEntityRenderer<TypewriterBlockEntity>{
    public TypewriterRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(TypewriterBlockEntity tileEntityIn, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = tileEntityIn.getBlockState();
        Direction facing = blockState.getValue(TypewriterBlock.HORIZONTAL_FACING);
   if(tileEntityIn.hasBlueprintIn()) {
       SuperByteBuffer blueprint = CachedBufferer.partial(CUBlockPartials.SCHEMATIC_MODEL, blockState);
       rotateCenteredInDirection(blueprint, Direction.UP, facing);
       blueprint.renderInto(ms, buffer.getBuffer(RenderType.solid()));
   }

        VertexConsumer builder = buffer.getBuffer(RenderType.cutoutMipped());



    }
    protected double getAngleForFacing(Direction facing) {
        double angle = 90 * (facing.equals(Direction.NORTH) ? 4 : facing.equals(Direction.SOUTH) ? 2  : facing.equals(Direction.EAST) ? 3 : 1);
        return angle;
    }
    protected void rotateCenteredInDirection(SuperByteBuffer model, Direction direction, Direction facing) {
        model.rotateCentered(direction, (float)  Math.toRadians(getAngleForFacing(facing)));
    }
}
