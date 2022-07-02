package com.fallenreaper.createutilities.content.blocks.steering_wheel;

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