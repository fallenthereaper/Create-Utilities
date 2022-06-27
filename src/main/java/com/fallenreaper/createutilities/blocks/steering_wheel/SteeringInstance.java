package com.fallenreaper.createutilities.blocks.steering_wheel;


import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.content.contraptions.components.crank.HandCrankBlock;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.stringtemplate.v4.ST;
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