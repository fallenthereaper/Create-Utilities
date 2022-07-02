package com.fallenreaper.createutilities.content.blocks.steering_wheel;


/*
public class SteeringInstance extends SingleRotatingInstance implements DynamicInstance {

    private final SteeringWheelBlockEntity tile;
    private ModelData crank;
    private Direction facing;

    public SteeringInstance(MaterialManager modelManager, SteeringWheelBlockEntity tile) {
        super(modelManager, tile);
        this.tile = tile;


        Block block = blockState.getBlock();
        PartialModel renderedHandle = null;
        if (block instanceof SteeringWheelBlock)
            renderedHandle = ((SteeringWheelBlock) block).getRenderedHandle();
        if (renderedHandle == null)
            return;

        facing = blockState.getValue(BlockStateProperties.FACING);
        Direction opposite = facing.getOpposite();
        Instancer<ModelData> model = getTransformMaterial().getModel(renderedHandle, opposite);
        crank = model.createInstance();

        rotateCrank();
    }

    @Override
    public void beginFrame() {
        if (crank == null) return;

        rotateCrank();
    }

    private void rotateCrank() {
        Direction.Axis axis = facing.getAxis();
        float angle = (tile.independentAngle + AnimationTickHolder.getPartialTicks() * tile.chasingVelocity) / 360;

        crank.loadIdentity()
                .translate(getInstancePosition())
                .centre()
                .rotate(Direction.get(Direction.AxisDirection.POSITIVE, axis), angle)
                .unCentre();
    }

    @Override
    public void remove() {
        super.remove();
        if (crank != null) crank.delete();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        if (crank != null) relight(pos, crank);
    }
*/