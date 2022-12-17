package com.fallenreaper.createutilities.content.blocks.bellow;

import com.fallenreaper.createutilities.index.CUBlockPartials;
import com.fallenreaper.createutilities.core.utils.MiscUtil;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.util.Mth;

public class BellowInstance extends ShaftInstance implements DynamicInstance {
    protected final BellowBlockEntity tile;
    protected final OrientedData bellowTop;
    protected final OrientedData bellowMiddle;
    protected final OrientedData bellowBottom;

    public BellowInstance(MaterialManager dispatcher, KineticTileEntity tile) {
        super(dispatcher, tile);
        this.tile = (BellowBlockEntity) tile;
        this.bellowTop = this.getOrientedMaterial().getModel(CUBlockPartials.BELLOWS, this.blockState).createInstance();
        this.bellowMiddle = this.getOrientedMaterial().getModel(CUBlockPartials.BELLOWS, this.blockState).createInstance();
        this.bellowBottom = this.getOrientedMaterial().getModel(CUBlockPartials.BELLOWS, this.blockState).createInstance();
        updatePosition();
        this.relight(this.pos, this.bellowTop, this.bellowMiddle, this.bellowBottom);

    }

    protected void updatePosition() {
        float timer = (AnimationTickHolder.getPartialTicks());
        double value = MiscUtil.lerp(0, 2, Mth.clamp(timer, -1, 1));
        Vector3f position = new Vector3f(0, 4, 0);
        this.bellowBottom.setPosition(position);
        this.bellowMiddle.setPosition(position);
        this.bellowTop.setPosition(position);


    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public void beginFrame() {
        updatePosition();
    }
}
