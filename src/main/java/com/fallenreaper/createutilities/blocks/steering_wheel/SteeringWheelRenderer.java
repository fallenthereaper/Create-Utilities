package com.fallenreaper.createutilities.blocks.steering_wheel;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.components.crank.HandCrankBlock;
import com.simibubi.create.content.contraptions.components.crank.HandCrankTileEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.bearing.BearingBlock;

import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
/*
public class SteeringWheelRenderer extends KineticTileEntityRenderer {
    public SteeringWheelRenderer(BlockEntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void renderSafe(KineticTileEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);


        if (Backend.canUseInstancing(te.getLevel())) return;

        BlockState state = te.getBlockState();
        Block block = state.getBlock();
        PartialModel renderedHandle = null;
        if (block instanceof SteeringWheelBlock)
            renderedHandle = ((SteeringWheelBlock) block).getRenderedHandle();
        if (renderedHandle == null)
            return;

        Direction facing = state.getValue(FACING);
        SuperByteBuffer handle = CachedBufferer.partialFacing(renderedHandle, state, facing.getOpposite());
        SteeringWheelBlockEntity crank = (SteeringWheelBlockEntity) te;

        kineticRotationTransform(handle, te, facing.getAxis(),
                (crank.independentAngle + partialTicks * crank.chasingVelocity) / 360, light);
        handle.renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));


       // standardKineticRotationTransform(shaftHalf, te, lightBehind).renderInto(ms, vb);



    }

    @Override
    protected SuperByteBuffer getRotatedModel(KineticTileEntity te, BlockState state) {
        return super.getRotatedModel(te, state);
    }

}
*/