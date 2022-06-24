package com.fallenreaper.createutilities.blocks.sprinkler;


import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.flwdata.RotatingData;
import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import net.minecraft.core.Direction;

public class SprinklerInstance extends ShaftInstance implements DynamicInstance {
    protected final SprinklerBlockEntity tile;

    protected final ModelData propagatorModelData;

    public SprinklerInstance(MaterialManager dispatcher, KineticTileEntity tile) {
        super(dispatcher, tile);
        this.tile = (SprinklerBlockEntity) tile;
        propagatorModelData = getTransformMaterial().getModel(CUBlockPartials.SPRINKLER_PROPAGATOR, Direction.EAST).createInstance();
    }


    @Override
    protected Instancer<RotatingData> getModel() {
        return super.getModel();
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos, propagatorModelData);
    }
    @Override
    public void remove() {
        super.remove();
        propagatorModelData.delete();
    }

    @Override
    public void beginFrame() {

    }
}
