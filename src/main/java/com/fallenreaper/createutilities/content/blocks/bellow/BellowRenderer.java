package com.fallenreaper.createutilities.content.blocks.bellow;

import com.fallenreaper.createutilities.index.CUBlockEntities;
import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ClientRegistry;

public class BellowRenderer extends KineticTileEntityRenderer {

    public BellowRenderer(BlockEntityRendererProvider.Context context) {

        super(context);
    }

    @Override
    protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = te.getBlockState();
        Block block = blockState.getBlock();
        if(block instanceof BellowBlock bellowBlock) {
            Direction facing = blockState.getValue(BellowBlock.HORIZONTAL_FACING);
            SuperByteBuffer bellows = CachedBufferer.partial(bellowBlock.getPartialModel(), blockState);
            rotateCenteredInDirection(bellows, Direction.UP, facing);
            bellows.renderInto(ms, buffer.getBuffer(RenderType.solid()));
            KineticTileEntityRenderer.renderRotatingKineticBlock(te, this.getRenderedBlockState(te), ms, buffer.getBuffer(RenderType.solid()), light);

        }


    }
    protected double getAngleForFacing(Direction facing) {
        double angle = 90 * (facing.equals(Direction.NORTH) ? 4 : facing.equals(Direction.SOUTH) ? 2  : facing.equals(Direction.EAST) ? 3 : 1);
        return angle;
    }
    protected void rotateCenteredInDirection(SuperByteBuffer model, Direction direction, Direction facing) {
        model.rotateCentered(direction, (float)  Math.toRadians(getAngleForFacing(facing)));
    }
    @Override
    public int getViewDistance() {
        return 64;
    }
    @Override
    protected BlockState getRenderedBlockState(KineticTileEntity te) {
        return shaft(getRotationAxisOf(te));
    }

}
